package com.hospital.appointment.service;

import com.hospital.appointment.model.Notification;
import com.hospital.appointment.model.User;
import com.hospital.appointment.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    public List<Notification> getUserNotifications(String username) {
        User user = (User) userService.loadUserByUsername(username);
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadNotifications(String username) {
        User user = (User) userService.loadUserByUsername(username);
        return notificationRepository.findByUserAndIsRead(user, false);
    }

    public long getUnreadNotificationCount(String username) {
        User user = (User) userService.loadUserByUsername(username);
        return notificationRepository.countByUserAndIsRead(user, false);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    public void createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }
}