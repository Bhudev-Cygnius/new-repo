package com.myorg.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.myorg.model.Session;
import com.myorg.service.SessionService;
import com.google.gson.Gson;
/**
 * Handler for creating a new session.
 * This Lambda function is triggered by an API Gateway POST request to /session/therapist/{therapistId}.
 */
public class CreateSessionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    // Service layer to handle business logic
    private final SessionService sessionService = new SessionService();
    private final Gson gson = new Gson();

    /**
     * Handles the incoming request to create a session.
     *
     * @param input   The API Gateway request event containing the request body and path parameters.
     * @param context The Lambda execution context.
     * @return The API Gateway response event containing the created session.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        // Log the incoming request for debugging
        context.getLogger().log("Received request to create session: " + input);

        // Extract the therapistId from the path parameters
        String therapistId = input.getPathParameters().get("therapistId");

        // Parse the request body into a Session object
        Session session = gson.fromJson(input.getBody(), Session.class);

        // Set the therapistId in the session object
        session.setTherapistId(therapistId);

        // Call the service to create the session
        Session createdSession = sessionService.createSession(session);

        // Log the response for debugging
        context.getLogger().log("Session created successfully: " + createdSession);

        // Convert the created session to JSON
        String responseBody = gson.toJson(createdSession);

        // Return the response with status code 201 (Created)
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(201)
                .withBody(responseBody);
    }
}