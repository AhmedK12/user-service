package com.healthaiharbor.ai.userservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Service responsible for logging user activities and publishing events to Kafka.
 */
@Service
public class UserActivityLogger {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityLogger.class);

    private final ActivityPublisher activityPublisher;

    /**
     * Constructor to initialize the UserActivityLogger with an activity publisher.
     *
     * @param activityPublisher The activity event publisher.
     */
    public UserActivityLogger(ActivityPublisher activityPublisher) {
        this.activityPublisher = activityPublisher;
    }

    /**
     * Logs a login event for a user.
     *
     * @param username The username of the logged-in user.
     */
    public void logLogin(String username) {
        logAndPublish(username, UserActivityType.LOGIN);
    }

    /**
     * Logs a logout event for a user.
     *
     * @param username The username of the logged-out user.
     */
    public void logLogout(String username) {
        logAndPublish(username, UserActivityType.LOGOUT);
    }

    /**
     * Logs a profile update event for a user.
     *
     * @param username The username of the user updating their profile.
     */
    public void updateProfile(String username) {
        logAndPublish(username, UserActivityType.PROFILE_UPDATE);
    }

    /**
     * Logs a token refresh event for a user.
     *
     * @param username The username of the user refreshing their token.
     */
    public void refreshToken(String username) {
        logAndPublish(username, UserActivityType.TOKEN_REFRESH);
    }

    /**
     * Logs a password change event for a user.
     *
     * @param username The username of the user changing their password.
     */
    public void passwordChange(String username) {
        logAndPublish(username, UserActivityType.PASSWORD_CHANGE);
    }

    /**
     * Logs a registration event for a new user.
     *
     * @param username The username of the newly registered user.
     */
    public void register(String username) {
        logAndPublish(username, UserActivityType.REGISTER);
    }

    /**
     * Helper method to log and publish user activity events.
     *
     * @param username      The username associated with the activity.
     * @param activityType  The type of activity being logged.
     */
    private void logAndPublish(String username, UserActivityType activityType) {
        UserActivityEvent event = UserActivityEvent.builder()
                .username(username)
                .activityType(activityType)
                .timestamp(Instant.now())
                .build();

        try {
            activityPublisher.publish(event);
            logger.info("User activity logged: {}", event);
        } catch (Exception e) {
            logger.error("Failed to log user activity: {}", event, e);
        }
    }
}
