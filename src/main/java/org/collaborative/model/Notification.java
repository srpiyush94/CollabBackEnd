package org.collaborative.model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "notificationType", "notificationReferenceId" } ) })
public class Notification {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String notificationType;
	private int notificationReferenceId;
	private String notificationDesc;
	private int userId;
	private String approvalStatus;
	private String rejectionReason;
	private boolean viewed;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public boolean isViewed() {
		return viewed;
	}
	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public int getNotificationReferenceId() {
		return notificationReferenceId;
	}
	public void setNotificationReferenceId(int notificationReferenceId) {
		this.notificationReferenceId = notificationReferenceId;
	}
	public String getNotificationDesc() {
		return notificationDesc;
	}
	public void setNotificationDesc(String notificationDesc) {
		this.notificationDesc = notificationDesc;
	}
	
	
}
