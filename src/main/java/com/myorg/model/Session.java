package com.myorg.model;


/**
 * Represents a session in the system.
 */
public class Session {
    private String sessionId;       // Unique identifier for the session
    private String clientId;       // ID of the client associated with the session
    private String therapistId;    // ID of the therapist associated with the session
    private String datetime;       // Date and time of the session
    private String status;         // Status of the session (e.g., Booked, Available, Completed)
    private String privateNotes;   // Private notes for the therapist
    private String sharedNotes;    // Shared notes with the client

    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    public String getSharedNotes() {
        return sharedNotes;
    }

    public void setSharedNotes(String sharedNotes) {
        this.sharedNotes = sharedNotes;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", therapistId='" + therapistId + '\'' +
                ", datetime='" + datetime + '\'' +
                ", status='" + status + '\'' +
                ", privateNotes='" + privateNotes + '\'' +
                ", sharedNotes='" + sharedNotes + '\'' +
                '}';
    }
}
