package com.myorg.service;
import com.myorg.repository.SessionRepository;
import com.myorg.model.Session;


import java.util.List;
import java.util.UUID;

/**
 * Service layer for handling session-related business logic.
 */
public class SessionService {

    private final SessionRepository sessionRepository = new SessionRepository();

    /**
     * Creates a new session.
     *
     * @param session The session details to create.
     * @return The created session.
     */
    public Session createSession(Session session) {
        // Generate a unique sessionId
        session.setSessionId(UUID.randomUUID().toString());
        

        // Set default status if not provided
        if (session.getStatus() == null) {
            session.setStatus("Available");
        }

        // Save the session to DynamoDB
        sessionRepository.saveSession(session);

        return session;
    }

    /**
     * Modifies an existing session.
     *
     * @param session The session details to modify.
     * @return The modified session.
     */
    public Session modifySession(Session session) {
        // Update the session in DynamoDB
        sessionRepository.saveSession(session);
        return session;
    }

    /**
     * Handles a client's request to book a session.
     *
     * @param session The session details to book.
     * @return The updated session.
     */
    public Session requestSession(Session session) {
        // Update the session status to "Booked"
        session.setStatus("Booked");

        // Save the updated session to DynamoDB
        sessionRepository.saveSession(session);

        return session;
    }

    /**
     * Retrieves all available sessions.
     *
     * @return A list of available sessions.
     */
    public List<Session> getAvailableSessions() {
        // Query DynamoDB for sessions with status "Available"
        return sessionRepository.findSessionsByStatus("Available");
    }

    
    public List<Session> searchSessions(String keyword) {
        // Query DynamoDB for sessions containing the keyword in sharedNotes
        return sessionRepository.findSessionsByKeyword(keyword);
    }
}
