package org.collaborative.model;

import java.util.*;

import javax.persistence.*;
@Entity
@Table(name="user_blogcomment")
public class Comment {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@ManyToOne
	private Post blogPost;
	@ManyToOne
	private User commentedBy;
	private Date commentedOn;
	private String commentTxt;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Post getBlogPost() {
		return blogPost;
	}
	public void setBlogPost(Post blogPost) {
		this.blogPost = blogPost;
	}
	public User getCommentedBy() {
		return commentedBy;
	}
	public void setCommentedBy(User commentedBy) {
		this.commentedBy = commentedBy;
	}
	public Date getCommentedOn() {
		return commentedOn;
	}
	public void setCommentedOn(Date commentedOn) {
		this.commentedOn = commentedOn;
	}
	public String getCommentTxt() {
		return commentTxt;
	}
	public void setCommentTxt(String commentTxt) {
		this.commentTxt = commentTxt;
	}
	
}