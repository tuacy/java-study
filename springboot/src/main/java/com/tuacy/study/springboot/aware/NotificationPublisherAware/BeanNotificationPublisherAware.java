package com.tuacy.study.springboot.aware.NotificationPublisherAware;

import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class BeanNotificationPublisherAware implements NotificationPublisherAware {

    private NotificationPublisher notificationPublisher;

    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
}
