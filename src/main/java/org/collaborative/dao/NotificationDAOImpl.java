package org.collaborative.dao;

import java.util.*;

import javax.transaction.Transactional;

import org.collaborative.model.Notification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class NotificationDAOImpl implements NotificationDAO {
	
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<Notification> getNotification(int userId, int viewed) {
		
		boolean viewStatus;
		
		if(viewed==0){
			viewStatus = false;
		}else{
			viewStatus = true;
		}
		Session session = sessionFactory.getCurrentSession();
		List<Notification> notificationList= session.createQuery(" from Notification where userId = ? and viewed = ?",Notification.class)
				 .setParameter(0, userId).setParameter(1, viewStatus).list();
		
		return notificationList;
	}

	@Transactional
	public Notification updateNotification(int notificationId) {
		Session session = sessionFactory.getCurrentSession();
		Notification notification= (Notification) session.get(Notification.class, notificationId);
		notification.setViewed(true);
		session.update(notification);
		return notification;
	}

	@Transactional
	public boolean addNotification(Notification notification) {
		Session session = sessionFactory.getCurrentSession();
		try{
			session.save(notification);
			return true;
		}catch(Exception e){
		return false;
		
		}
	}

	@Transactional
	public Notification getNotification(String notificationType,int notificationReferenceId) {
				
		Session session = sessionFactory.getCurrentSession();
		Notification notification= (Notification) session.createQuery(" from Notification where notificationType = ? and notificationReferenceId = ?",Notification.class)
				 .setParameter(0, notificationType).setParameter(1, notificationReferenceId).uniqueResult();
		
		return notification;
	}

	@Transactional
	public boolean deleteNotification(Notification notification) {
		try
		{
			sessionFactory.getCurrentSession().delete(notification);
			return true;
			
		}catch(Exception e){
			
			System.out.println("Exception raised: "+e);
			return false;
		}
	}

}