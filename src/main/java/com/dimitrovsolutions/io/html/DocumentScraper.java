package com.dimitrovsolutions.io.html;

import com.dimitrovsolutions.context.Context;
import com.dimitrovsolutions.model.Job;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Responsible for traversing html document, filtering out new jobs listings and adding them
 * to jobsCache.
 */
public class DocumentScraper {

    private static final String HTML_STRUCTURE_CHANGE = "Html element doesn't exist anymore";

    public static final Logger logger = Logger.getLogger(DocumentScraper.class.getName());
    private static final FileHandler fileHandler;
    private static final String LOG_DIRECTORY = "src/main/resources/logs/html_mapper.log";

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

    /**
     * Traverses jobs listings elements in html document and filters out new job listings adding them
     * to jobsCache, and skipping all old listings.
     */
    public static void traverseDocumentAndAddNewJobsToJobsCache(Context context) {
        logger.log(Level.INFO, "Html response body to Map<Integer,Job> starting at: " + LocalDateTime.now());

        try {
            // DIV with JOB ID
            Elements jobsIds = getElementsByCssQuery(context.getDocument(), "div[data-job-id]");

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

                // inner div, then inner <a> tag
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
                Attribute linkAttribute = attributes1.attribute("href");
                String jobUrl = linkAttribute.getValue();
                if (isTitleAndUrlValid(title, jobUrl)) {
                    Job job = new Job(title, jobUrl, LocalDateTime.now());

                    // TODO this line might be useless or harmful, write tests
                    if (context.getAlreadyAppliedCache().containsJobId(jobId)) {
                        continue;
                    }

                    if (jobAlreadyPosted(context, job)) {
                        logger.log(Level.CONFIG, "Job has been reposted");

                        List<Job> jobs = context.getAlreadyAppliedCache().fetchJobsInDescendingOrder(job.title());

                        // Last posted job with such title and url
                        Job repostedJob = jobs.get(0);
                        tryApplyToRepostedJob(context, repostedJob, job, jobId);
                        continue;
                    }

                    logger.log(Level.CONFIG, "Applying to new job posting " + job);
                    context.tryAddJobToJobsCache(jobId, job);
                }
            }
        } finally {
            logger.log(Level.INFO, "Html response body to Map<Integer,Job> ends at: " + LocalDateTime.now());
        }
    }

    private static boolean jobAlreadyPosted(Context context, Job job) {
        return context.getAlreadyAppliedCache().getCache().containsValue(job);
    }

    private static boolean isTitleAndUrlValid(String title, String url) {
        return title != null && !title.isEmpty() && url != null && !url.isEmpty();
    }

    /**
     * If reposted job is older than 90 days, tries to add the new job with the Same title and Url to
     * the jobsCache
     */
    private static void tryApplyToRepostedJob(Context context, Job repostedJob, Job newJob, int jobId) {
        if (repostedJob.olderThanThreeMonths()) {
            logger.log(Level.CONFIG, "Re-applied to reposted job " + newJob);
            context.getJobsCache().getCache().putIfAbsent(jobId, newJob);
        }
    }

    /**
     * Return Elements corresponding to sqlQuery
     */
    private static Elements getElementsByCssQuery(Document currentDocument, String cssQuery) {
        return currentDocument.select(cssQuery);
    }

    /**
     * FileHandler teardown used in finally block of Orchestrator
     */
    public static void tearDown() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }
}