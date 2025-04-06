package com.healthaiharbor.ai.userservice.kafka;

public interface ActivityPublisher {
    void publish(UserActivityEvent event);
}
