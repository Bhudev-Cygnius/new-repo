package com.myorg.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.myorg.model.Session;
import com.myorg.service.SessionService;
import com.google.gson.Gson;

/**
 * Handler for a client requesting to book a session.
 * This Lambda function is triggered by an API Gateway PUT request to /session/{sessionId}/client/{clientId}.
 */
public class RequestSessionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    // Service layer to handle business logic
    private final SessionService sessionService = new SessionService();
    private final Gson gson = new Gson();

    /**
     * Handles the incoming request to book a session.
     *
     * @param input   The API Gateway request event containing the request body and path parameters.
     * @param context The Lambda execution context.
     * @return The API Gateway response event containing the updated session.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        // Log the incoming request for debugging
        context.getLogger().log("Received request to book session: " + input);

        // Extract path parameters
        String sessionId = input.getPathParameters().get("sessionId");
        String clientId = input.getPathParameters().get("clientId");

        // Parse the request body into a Session object
        Session session = gson.fromJson(input.getBody(), Session.class);

        // Set the sessionId and clientId in the session object
        session.setSessionId(sessionId);
        session.setClientId(clientId);

        // Call the service to handle the session request
        Session requestedSession = sessionService.requestSession(session);

        // Log the response for debugging
        context.getLogger().log("Session request processed: " + requestedSession);

        // Convert the updated session to JSON
        String responseBody = gson.toJson(requestedSession);

        // Return the response with status code 200 (OK)
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(responseBody);
    }
}
