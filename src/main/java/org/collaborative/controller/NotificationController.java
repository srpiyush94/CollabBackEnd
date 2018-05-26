package org.collaborative.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.collaborative.model.ErrorClass;
import org.collaborative.model.Notification;
import org.collaborative.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;
	
	private static Logger log = LoggerFactory.getLogger(NotificationController.class);
	
	@RequestMapping(value="/getnotification/{viewed}")
	public ResponseEntity<?> getNotification(@PathVariable("viewed")int viewed,HttpSession session){
		log.info("get notification");
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null){
			  
			    return new ResponseEntity<ErrorClass>(new ErrorClass(7,"User session details not found"),HttpStatus.UNAUTHORIZED);
		}	   
		else {
			List<Notification> notificationList = notificationService.getNotification(userId, viewed);
			
			if(notificationList == null){
			
				return new ResponseEntity<ErrorClass>(new ErrorClass(51,"Notification details not found"),HttpStatus.INTERNAL_SERVER_ERROR);
			}else{
				
				return new ResponseEntity<List<Notification>>(notificationList,HttpStatus.OK);
			}
		}
	}
	
	
	@RequestMapping(value="/updatenotification/{notificationId}")
	public ResponseEntity<?> updateNotification(@PathVariable("notificationId")int notificationId,HttpSession session){

		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null){
			  
			    return new ResponseEntity<ErrorClass>(new ErrorClass(7,"User session details not found"),HttpStatus.UNAUTHORIZED);
		}	   
		else {
			Notification notification = notificationService.updateNotification(notificationId);
			
			if(notification == null){
			
				return new ResponseEntity<ErrorClass>(new ErrorClass(51,"Notification details not found"),HttpStatus.INTERNAL_SERVER_ERROR);
			}else{
				
				return new ResponseEntity<Notification>(notification,HttpStatus.OK);
			}
		}
	}
	
	
}



