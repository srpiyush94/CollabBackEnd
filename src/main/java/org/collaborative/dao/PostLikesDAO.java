package org.collaborative.dao;

import org.collaborative.model.*;

public interface PostLikesDAO {
	
	public PostLikes getUserBlogPostLikes(Post post,User user);
	
	public Post updateBlogPostLikes(Post blog,User user);
	

}
