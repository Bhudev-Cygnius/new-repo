package com.myorg.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.myorg.model.Session;
import com.myorg.service.SessionService;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Handler for searching sessions by a keyword in therapist notes.
 * This Lambda function is triggered by an API Gateway GET request to /sessions/search?keyword={keyword}.
 */
public class SearchSessionsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    // Service layer to handle business logic
    private final SessionService sessionService = new SessionService();
    private final Gson gson = new Gson();

    /**
     * Handles the incoming request to search sessions.
     *
     * @param input   The API Gateway request event containing the query parameters.
     * @param context The Lambda execution context.
     * @return The API Gateway response event containing the list of matching sessions.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        // Log the incoming request for debugging
        context.getLogger().log("Received request to search sessions");

        // Extract the keyword from query parameters
        Map<String, String> queryParameters = input.getQueryStringParameters();
        String keyword = queryParameters.get("keyword");

        // Call the service to search sessions
        List<Session> matchingSessions = sessionService.searchSessions(keyword);

        // Log the response for debugging
        context.getLogger().log("Matching sessions: " + matchingSessions);

        // Convert the list of sessions to JSON
        String responseBody = gson.toJson(matchingSessions);

        // Return the response with status code 200 (OK)
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(responseBody);
    }
}