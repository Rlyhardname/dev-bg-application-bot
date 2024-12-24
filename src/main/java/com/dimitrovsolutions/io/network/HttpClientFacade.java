package com.dimitrovsolutions.io.network;

import com.dimitrovsolutions.io.Destructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * HttpClientWrapper abstracting HttpClient and corresponding Logger creation, exposing just
 * needed http methods.
 */
public class HttpClientFacade implements Destructor {

    public static final Logger logger = Logger.getLogger(HttpClientFacade.class.getName());
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final FileHandler fileHandler;

    public HttpClientFacade() {
        try {
            String LOG_DIRECTORY = "E:\\jobsCache\\httpLog.txt";

            this.fileHandler = new FileHandler(LOG_DIRECTORY, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.log(Level.INFO, "HttpClient logger start at: " + LocalDateTime.now());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds new HttpRequest from string url object.
     */
    public HttpRequest getRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
    }

    /**
     * Sends HttpAsync request to
     */
    public HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
        } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
            String HTTP_REQUEST_ERROR = e.getMessage() + " at: " + LocalDateTime.now();

            logger.log(Level.SEVERE, HTTP_REQUEST_ERROR);
            throw new HttpRequestException();
        }

    }

    /**
     * Used in finally block of Orchestrator
     */
    @Override
    public void tearDown() {
        logger.log(Level.INFO, "HttpClient teardown at: " + LocalDateTime.now());

        if (fileHandler != null) {
            fileHandler.close();
        }
    }
}