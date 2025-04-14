package com.myorg.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.myorg.model.Session;
import com.myorg.service.SessionService;
import com.google.gson.Gson;

import java.util.List;

/**
 * Handler for retrieving all available sessions.
 * This Lambda function is triggered by an API Gateway GET request to /session/available.
 */
public class GetAvailableSessionsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    // Service layer to handle business logic
    private final SessionService sessionService = new SessionService();
    private final Gson gson = new Gson();

    /**
     * Handles the incoming request to retrieve available sessions.
     *
     * @param input   The API Gateway request event.
     * @param context The Lambda execution context.
     * @return The API Gateway response event containing the list of available sessions.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        // Log the incoming request for debugging
        context.getLogger().log("Received request to get available sessions");

        // Call the service to retrieve available sessions
        List<Session> availableSessions = sessionService.getAvailableSessions();

        // Log the response for debugging
        context.getLogger().log("Available sessions: " + availableSessions);

        // Convert the list of sessions to JSON
        String responseBody = gson.toJson(availableSessions);

        // Return
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(responseBody);
    }
}