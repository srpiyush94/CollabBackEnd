package org.collaborative.service;

import org.collaborative.dao.PostLikesDAO;
import org.collaborative.model.Post;
import org.collaborative.model.PostLikes;
import org.collaborative.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PostLikesServiceImpl implements PostLikesService {

	@Autowired
	
	private PostLikesDAO postLikesDAO;
	

	public PostLikes getUserBlogPostLikes(Post post, User user) {
		
		return postLikesDAO.getUserBlogPostLikes(post, user);
	}

	public Post updateBlogPostLikes(Post blog, User user) {
		
		return postLikesDAO.updateBlogPostLikes(blog, user);
	}

}
