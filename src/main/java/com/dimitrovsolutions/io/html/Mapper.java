package com.dimitrovsolutions.io.html;

import com.dimitrovsolutions.context.Context;
import com.dimitrovsolutions.model.Job;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Mapper {
    public static final Logger logger = Logger.getLogger(Mapper.class.getName());
    private static final FileHandler fileHandler;
    private static final String LOG_DIRECTORY = "src/main/resources/logs/html_mapper.log";
    private static final String HTML_STRUCTURE_CHANGE = "Html element doesn't exist anymore";

    static {
        try {
            fileHandler = new FileHandler(LOG_DIRECTORY, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void toMap(Context context) {
        logger.log(Level.INFO, "Html response body to Map<Integer,Job> starting at: " + LocalDateTime.now());

        try {
            // DIV with JOB ID
            Elements jobsIds = context.getDocument().select("div[data-job-id]");

            for (Element el : jobsIds) {
                Attributes attributes = el.attributes();
                // JOB ID for map
                Integer jobId = Integer.parseInt(attributes.attribute("data-job-id").getValue());
                var child1 = el.children().get(1);
                //System.out.println("CHILD 1 val: " + child1.attributes().asList().get(0).getValue());
                var child2 = child1.children().get(2);
                //System.out.println("CHILD 2 val: " + child2.attributes().asList().get(0).getValue());
                var child3 = child2.children().get(0);
                //System.out.println("CHILD 3 val: " + child3.text());
                String title = child3.text();

                // inner dive, then inner <a> tag
                Element child = el.firstElementChild();
                if (child == null) {
                    logger.log(Level.INFO, HTML_STRUCTURE_CHANGE);
                    continue;
                }

                child = child.firstElementChild();
                if (child == null) {
                    logger.log(Level.INFO, HTML_STRUCTURE_CHANGE);
                    continue;
                }

                Attributes attributes1 = child.attributes();
                Attribute attribute1 = attributes1.attribute("href");
                String jobUrl = attribute1.getValue();
                if (title != null && !title.isEmpty() && jobUrl != null && !jobUrl.isEmpty()) {
                    Job job = new Job(title, jobUrl, LocalDateTime.now());

                    if (context.getAlreadyAppliedCache().getCache().containsKey(jobId)) {
                        continue;
                    }

                    if (context.getAlreadyAppliedCache().getCache().containsValue(job)) {
                        logger.log(Level.CONFIG, "Job has been reposted");

                        List<Job> jobs = context.getAlreadyAppliedCache().fetchJobsInDescendingOrder(job.title());
                        Job repostedJob = jobs.get(0);
                        if (repostedJob.olderThanThreeMonths()) {
                            logger.log(Level.CONFIG, "Re-applied to reposted job " + job);
                            context.getJobsCache().getCache().putIfAbsent(jobId, job);
                        }

                        continue;
                    }

                    logger.log(Level.CONFIG, "Applying to new job posting " + job);
                    context.getJobsCache().getCache().putIfAbsent(jobId, job);

                }

            }
        } finally {
            logger.log(Level.INFO, "Html response body to Map<Integer,Job> ends at: " + LocalDateTime.now());
        }
    }

    public static void tearDown() {
        if (fileHandler != null) {
            fileHandler.close();
        }

    }
}
