package com.dimitrovsolutions.io.network;

import com.dimitrovsolutions.io.Destructor;
import com.dimitrovsolutions.util.LoggerUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dimitrovsolutions.util.LoggerUtil.hasNoHandlers;
import static com.dimitrovsolutions.util.LoggerUtil.initLogger;

/**
 * HttpClientWrapper abstracting HttpClient and corresponding Logger creation, exposing just
 * needed http methods.
 */
public class HttpClientFacade implements Destructor{

    private static final Logger logger = Logger.getLogger(HttpClientFacade.class.getName());
    private static final String HTTP_CLIENT_LOGGER_FILE_NAME = "/http_client.log";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public HttpClientFacade() {
        if (hasNoHandlers(logger)) {
            initLogger(logger, Level.ALL, HTTP_CLIENT_LOGGER_FILE_NAME);
        }

        logger.log(Level.INFO, "HttpClient logger start at: " + LocalDateTime.now());
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
        LoggerUtil.tearDown(logger);
    }
}