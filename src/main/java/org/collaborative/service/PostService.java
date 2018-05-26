package org.collaborative.service;

import java.util.List;

import org.collaborative.model.Post;

public interface PostService {
	
public boolean addPost(Post post);
	
	public boolean updatePost(Post post);
	
	public boolean deletePost(Post post);
	
	public Post getPost(int blogId);
	
	public List<Post> getAllPost(int userId);
	
	public boolean approvePost(Post post);
	
	public boolean rejectPost(Post post);
	
	public List<Post> getAllPost();
	
	public List<Post> getAllPendingPost();
	
	public List<Post> getAllApprovedPost();
	
	public int updateNoOfLikes(Post post);
}
