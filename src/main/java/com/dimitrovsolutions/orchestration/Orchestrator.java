package com.dimitrovsolutions.orchestration;

import com.dimitrovsolutions.io.Destructor;
import com.dimitrovsolutions.io.automation.SeleniumFacade;
import com.dimitrovsolutions.context.Context;
import com.dimitrovsolutions.io.html.Mapper;
import com.dimitrovsolutions.io.files.JobLoader;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Orchestrator implements Destructor {
    private static Orchestrator orchestrator;
    private final Context context;
    private final FileHandler fileHandler;
    private static final Logger logger = Logger.getLogger(Orchestrator.class.getName());
    private static final String START_URL = "https://dev.bg/company/jobs/java/?_seniority=intern";
    private static final String LOG_DIRECTORY = "src/main/resources/logs/orchestrator.log";

    public static Context start() {
        if (orchestrator == null) {
            orchestrator = new Orchestrator();
        }

        return orchestrator.getContext();
    }

    private Orchestrator() {
        this.context = new Context();
        SeleniumFacade selenium = null;
        try {
            fileHandler = new FileHandler(LOG_DIRECTORY, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.log(Level.INFO, "Application started at: " + LocalDateTime.now());

            JobLoader.loadJobs(context.getAlreadyAppliedCache().getCache());

            collectApplicationUrls(START_URL);

            selenium = new SeleniumFacade(context, "chrome");
            selenium.runScript();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            logger.log(Level.INFO, "Application closing without errors: " + LocalDateTime.now());
            tearDownFileHandlers(selenium);
        }

    }

    private void collectApplicationUrls(String url) {
        HttpRequest request = context.getClient().getRequest(url);

        HttpResponse<String> response = context.getClient().send(request);

        String body = context.parseDocBody(response);
        context.setDocument(Jsoup.parse(body));

        print("BODY " + body);

        Mapper.toMap(context);
    }

    private static void print(String htmlResponseBody) {
        System.out.println(htmlResponseBody);
    }

    public void tearDownFileHandlers(SeleniumFacade selenium) {
        tearDown();

        if (selenium != null) {
            selenium.tearDown();
        }

        context.tearDown();
        Mapper.tearDown();
        JobLoader.tearDown();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void tearDown() {
        logger.log(Level.INFO, "Application logger tear down at: " + LocalDateTime.now());

        if (fileHandler != null) {
            fileHandler.close();
        }

    }
}