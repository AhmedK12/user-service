package com.healthaiharbor.ai.userservice.kafka;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

/**
 * Represents an event related to user activity, which can be logged or published via Kafka.
 */
@Builder
@Data
public class UserActivityEvent {

    /**
     * The username of the user whose activity is being recorded.
     */
    private String username;

    /**
     * The type of activity being recorded (e.g., LOGIN, LOGOUT, PASSWORD_CHANGE).
     */
    private UserActivityType activityType;

    /**
     * The user (e.g., an admin) who performed this action, if applicable.
     */
    private String performedBy;

    /**
     * The timestamp when the activity occurred.
     */
    private Instant timestamp;

    /**
     * Additional metadata related to the event, such as IP address, device details, or reasons for actions.
     */
    private Map<String, Object> metadata;
}
