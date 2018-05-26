package org.collaborative.service;

import java.util.List;

import org.collaborative.dao.NotificationDAO;
import org.collaborative.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	
	private NotificationDAO  notificationDAO;
	
	@Override
	public boolean addNotification(Notification notification) {
		
		return notificationDAO.addNotification(notification);
	}

	@Override
	public List<Notification> getNotification(int userId, int viewed) {
		
		return notificationDAO.getNotification(userId, viewed);
	}

	@Override
	public Notification updateNotification(int notificationId) {
		
		return notificationDAO.updateNotification(notificationId);
	}

	@Override
	public Notification getNotification(String notificationType, int notificationReferenceId) {
		
		return notificationDAO.getNotification(notificationType, notificationReferenceId);
	}

	@Override
	public boolean deleteNotification(Notification notification) {
		
		return notificationDAO.deleteNotification(notification);
	}

}
