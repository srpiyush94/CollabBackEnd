package org.collaborative.service;

import java.util.List;

import org.collaborative.dao.PostCommentDAO;
import org.collaborative.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PostCommentServiceImpl implements PostCommentService {

	@Autowired
	private PostCommentDAO postCommentDAO;

	@Override
	public boolean addPostComment(Comment comment) {

		return postCommentDAO.addPostComment(comment);
	}

	public boolean updatePostComment(Comment comment) {

		return postCommentDAO.updatePostComment(comment);
	}

	public boolean deletePostComment(Comment comment) {

		return postCommentDAO.deletePostComment(comment);
	}

	public Comment getPostComment(int blogCommentId) {

		return postCommentDAO.getPostComment(blogCommentId);
	}

	public List<Comment> getAllPostComments(int postid) {
		
		return postCommentDAO.getAllPostComments(postid);
	}

}
