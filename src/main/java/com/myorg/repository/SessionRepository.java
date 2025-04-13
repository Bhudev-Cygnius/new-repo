package com.myorg.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.myorg.model.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository layer for interacting with DynamoDB.
 */
public class SessionRepository {

    private final DynamoDB dynamoDB;

    public SessionRepository() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        this.dynamoDB = new DynamoDB(client);
    }

    /**
     * Saves a session to DynamoDB.
     *
     * @param session The session to save.
     */
    public void saveSession(Session session) {
        Table table = dynamoDB.getTable("SessionTable");

        Item item = new Item()
                .withPrimaryKey("sessionId", session.getSessionId())
                .withString("clientId", session.getClientId())
                .withString("therapistId", session.getTherapistId())
                .withString("datetime", session.getDatetime())
                .withString("status", session.getStatus())
                .withString("privateNotes", session.getPrivateNotes())
                .withString("sharedNotes", session.getSharedNotes());

        table.putItem(item);
    }

    /**
     * Finds sessions by status.
     *
     * @param status The status to search for.
     * @return A list of sessions with the specified status.
     */
    public List<Session> findSessionsByStatus(String status) {
        Table table = dynamoDB.getTable("SessionTable");

        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression("#status = :status")
                .withNameMap(new NameMap().with("#status", "status"))
                .withValueMap(new ValueMap().withString(":status", status));

 ItemCollection<ScanOutcome> items = table.scan(scanSpec);
        List<Session> sessions = new ArrayList<>();
        for (Item item : items) {
            sessions.add(mapItemToSession(item));
        }
        return sessions;
    }
    

    /**
     * Finds sessions containing a keyword in the shared notes.
     *
     * @param keyword The keyword to search for.
     * @return A list of sessions containing the keyword.
     */
    public List<Session> findSessionsByKeyword(String keyword) {
        Table table = dynamoDB.getTable("SessionTable");

        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression("contains(sharedNotes, :keyword)")
                .withValueMap(new ValueMap().withString(":keyword", keyword));

        ItemCollection<ScanOutcome> items = table.scan(scanSpec);
        List<Session> sessions = new ArrayList<>();
        for (Item item : items) {
            sessions.add(mapItemToSession(item));
        }
        return sessions;
    }

    /**
     * Maps a DynamoDB item to a Session object.
     *
     * @param item The DynamoDB item.
     * @return The mapped Session object.
     */
    private Session mapItemToSession(Item item) {
        Session session = new Session();
        session.setSessionId(item.getString("sessionId"));
        session.setClientId(item.getString("clientId"));
        session.setTherapistId(item.getString("therapistId"));
        session.setDatetime(item.getString("datetime"));
        session.setStatus(item.getString("status"));
        session.setPrivateNotes(item.getString("privateNotes"));
        session.setSharedNotes(item.getString("sharedNotes"));
        return session;
    }
}
