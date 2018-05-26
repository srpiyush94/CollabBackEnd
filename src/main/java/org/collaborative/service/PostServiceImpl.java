package org.collaborative.service;

import java.util.List;
import org.collaborative.dao.PostDAO;
import org.collaborative.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

	
	@Autowired
	
	private PostDAO postDAO;

	@Override
	public boolean addPost(Post post) {
		
		return postDAO.addPost(post);
	}

	@Override
	public boolean updatePost(Post post) {
	
		return postDAO.updatePost(post);
	}

	@Override
	public boolean deletePost(Post post) {
		
		return postDAO.deletePost(post);
	}

	@Override
	public Post getPost(int blogId) {
		
		return postDAO.getPost(blogId);
	}

	@Override
	public List<Post> getAllPost(int userId) {
		
		return postDAO.getAllPost(userId);
	}

	@Override
	public boolean approvePost(Post post) {
	
		return postDAO.approvePost(post);
	}

	@Override
	public boolean rejectPost(Post post) {
	
		return postDAO.rejectPost(post);
	}

	@Override
	public List<Post> getAllPost() {
		
		return postDAO.getAllPost();
	}

	@Override
	public List<Post> getAllPendingPost() {
	
		return postDAO.getAllPendingPost();
	}

	@Override
	public List<Post> getAllApprovedPost() {
		
		return postDAO.getAllApprovedPost();
	}

	@Override
	public int updateNoOfLikes(Post post) {
		
		return postDAO.updateNoOfLikes(post);
	}
	
}
