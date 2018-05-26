package org.collaborative.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import org.collaborative.model.Post;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class PostDAOImpl implements PostDAO {

	@Autowired

	private SessionFactory sessionFactory;

	@Override
	public boolean addPost(Post post) {
		
	try {
		sessionFactory.getCurrentSession().saveOrUpdate(post);
		return true;
	} catch (HibernateException e) {
		
		e.printStackTrace();
		return false;
	}

	}

	@Override
	public boolean updatePost(Post post) {
		try{
			sessionFactory.getCurrentSession().update(post);
			return true;
		}catch(Exception e){
			System.out.println("Exception raised: "+e);
			return false;
		}

	
	}

	@Override
	public boolean deletePost(Post post) {
		try
		{
			sessionFactory.getCurrentSession().delete(post);
			return true;
			
		}catch(Exception e){
			
			System.out.println("Exception raised: "+e);
			return false;
		}
	}

	@Override
	public Post getPost(int blogId) {
		Session session = sessionFactory.openSession();
		
		Post postObj = session.get(Post.class, blogId);
		
		session.close();
		
		return postObj;
	}

	@Override
	public List<Post> getAllPost(int userId) {
		Session session = sessionFactory.openSession();
		
		List<Post> blogList= session.createQuery("from Post where id = :userId",Post.class)
				.setParameter("userId", userId).list();
		
		session.close();
		
		return blogList;
	}

	@Override
	public boolean approvePost(Post post) {
	try{
			
			Post blogObj = sessionFactory.getCurrentSession().load(Post.class, post.getId());
			blogObj.setStatus("APPROVED");
			blogObj.setPublishDate(new Date());
			return true;
			
		}catch(Exception e){
			System.out.println("Exception raised: "+e);
			return false;
		}

	}
	@Override
	public boolean rejectPost(Post post) {
try{
			
		Post blogObj = sessionFactory.getCurrentSession().load(Post.class, post.getId());
			blogObj.setStatus("REJECTED");
			blogObj.setPublishDate(new Date());
			return true;
			
		  }catch(Exception e){
			System.out.println("Exception raised: "+e);
			return false;
		  }
	}

	@Override
	public List<Post> getAllPost() {
Session session = sessionFactory.openSession();
		
		List<Post> blogList= session.createQuery("from Post where status = 'APPROVED'",Post.class).list();
		
		session.close();
		
		return blogList;
	}

	@Override
	public List<Post> getAllPendingPost() {
Session session = sessionFactory.openSession();
		
		List<Post> blogList= session.createQuery("from Post where status = 'PENDING'",Post.class).list();
		
		session.close();
		
		return blogList;
	}

	@Override
	public List<Post> getAllApprovedPost() {
Session session = sessionFactory.openSession();
		
		List<Post> blogList= session.createQuery("from Post where status = 'APPROVED'",Post.class).list();
		
		session.close();
		
		return blogList;
	}

	@Override
	public int updateNoOfLikes(Post post) {
try{
			
		Post blogObj = sessionFactory.getCurrentSession().load(Post.class, post.getId());
			blogObj.setNoOfLikes(post.getNoOfLikes());
			return blogObj.getNoOfLikes();
			
		  }catch(Exception e){
			System.out.println("Exception raised: "+e);
			return 0;
		  }
	}


	
	
	
}