package com.healthaiharbor.ai.userservice.kafka;

import com.healthaiharbor.ai.userservice.modal.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Service responsible for logging administrative activities.
 * Publishes activity events to Kafka for tracking user management actions.
 */
@Service
public class AdminActivityLogger {

    private static final Logger logger = LoggerFactory.getLogger(AdminActivityLogger.class);
    private final ActivityPublisher activityPublisher;

    public AdminActivityLogger(ActivityPublisher activityPublisher) {
        this.activityPublisher = activityPublisher;
    }

    /**
     * Logs an account lock action.
     *
     * @param admin The admin performing the action.
     * @param user  The user whose account is locked.
     */
    public void accountLocked(User admin, User user) {
        logAndPublish(admin, user, UserActivityType.LOGIN, "locked");
    }

    /**
     * Logs an account unlock action.
     *
     * @param admin The admin performing the action.
     * @param user  The user whose account is unlocked.
     */
    public void accountUnlocked(User admin, User user) {
        logAndPublish(admin, user, UserActivityType.LOGOUT, "unlocked");
    }

    /**
     * Logs a role update action.
     *
     * @param admin The admin performing the action.
     * @param user  The user whose role is updated.
     */
    public void roleUpdated(User admin, User user) {
        logAndPublish(admin, user, UserActivityType.PROFILE_UPDATE, "updated role");
    }

    /**
     * Logs a permission granted action.
     *
     * @param admin The admin performing the action.
     * @param user  The user receiving new permissions.
     */
    public void permissionGranted(User admin, User user) {
        logAndPublish(admin, user, UserActivityType.TOKEN_REFRESH, "granted permissions");
    }

    /**
     * Logs a permission revocation action.
     *
     * @param admin The admin performing the action.
     * @param user  The user losing permissions.
     */
    public void permissionRevoked(User admin, User user) {
        logAndPublish(admin, user, UserActivityType.PASSWORD_CHANGE, "revoked permissions");
    }

    /**
     * Logs a new user registration by an admin.
     *
     * @param admin The admin performing the registration.
     * @param user  The user being registered.
     */
    public void registerByAdmin(User admin, User user) {
        logAndPublish(admin, user, UserActivityType.REGISTER, "registered");
    }

    /**
     * Logs an account deletion action.
     *
     * @param admin The admin performing the action.
     * @param user  The user being deleted.
     */
    public void deleted(User admin, User user) {
        logAndPublish(admin, user, UserActivityType.ACCOUNT_DELETE, "deleted");
    }

    /**
     * Logs when multiple users are fetched by an admin.
     *
     * @param admin The admin fetching users.
     * @param o     The fetched users object (can be a list or response object).
     */
    public void usersFetched(User admin, Object o) {
        logger.info("Admin '{}' fetched multiple users.", admin.getUsername());
    }

    /**
     * Logs when a single user is fetched by an admin.
     *
     * @param admin The admin fetching the user.
     * @param user  The fetched user.
     */
    public void userFetched(User admin, User user) {
        logger.info("Admin '{}' fetched user '{}'.", admin.getUsername(), user.getUsername());
    }

    /**
     * Helper method to log and publish an activity event.
     *
     * @param admin   The admin performing the action.
     * @param user    The affected user.
     * @param type    The activity type.
     * @param action  The action performed (for logging purposes).
     */
    private void logAndPublish(User admin, User user, UserActivityType type, String action) {
        logger.info("Admin '{}' {} user '{}'.", admin.getUsername(), action, user.getUsername());

        activityPublisher.publish(UserActivityEvent.builder()
                .username(user.getUsername())
                .activityType(type)
                .timestamp(Instant.now())
                .build());
    }
}
