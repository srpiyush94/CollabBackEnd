package org.collaborative.dao;

import javax.transaction.Transactional;

import org.collaborative.model.Post;
import org.collaborative.model.PostLikes;
import org.collaborative.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@SuppressWarnings("deprecation")
@Transactional
@Repository
public class PostLikesDAOImpl implements PostLikesDAO {
	
	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings({ "rawtypes" })
	public PostLikes getUserBlogPostLikes(Post post, User user) {

		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from PostLikes where Post.blogId = :blogId and user.c_user_id = :userId",PostLikes.class);
		query.setParameter("blogId", post.getId());
		query.setParameter("userId", user.getId());
		PostLikes blogPostLikes = (PostLikes) query.uniqueResult();
		
		return blogPostLikes;
	}
	@Override
	public Post updateBlogPostLikes(Post blog, User user) {
		Session session = sessionFactory.getCurrentSession();
		PostLikes blogPostLikes=getUserBlogPostLikes(blog,user);
		
		if(blogPostLikes == null){
			//insert into blogpostlikes, increment blogpost.likes
			PostLikes insertUserLike = new PostLikes();
			insertUserLike.setPost(blog);
			insertUserLike.setUser(user);
			session.save(insertUserLike); //insert into blogpostlikes
			 //increment the number of likes
			blog.setNoOfLikes(blog.getNoOfLikes() + 1);
			session.update(blog);//update blogpost set likes=.. where id=?
			
		}else{//unlike
			
			session.delete(blogPostLikes);
			//Decrement no of likes
			blog.setNoOfLikes(blog.getNoOfLikes() - 1);
			session.merge(blog); //update blogpost set likes ...
			
		}
		return blog;
	}

}
