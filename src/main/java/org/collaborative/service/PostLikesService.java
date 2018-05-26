package org.collaborative.service;

import org.collaborative.model.Post;
import org.collaborative.model.PostLikes;
import org.collaborative.model.User;

public interface PostLikesService {
	
	public PostLikes getUserBlogPostLikes(Post post,User user);
	
	public Post updateBlogPostLikes(Post blog,User user);

}
