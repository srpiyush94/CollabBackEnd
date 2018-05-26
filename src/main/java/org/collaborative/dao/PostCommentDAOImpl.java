package org.collaborative.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.collaborative.model.Comment;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public class PostCommentDAOImpl implements PostCommentDAO {

	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public boolean addPostComment(Comment comment) {
	
	 try {
		sessionFactory.getCurrentSession().saveOrUpdate(comment);
		return true;
	} catch (HibernateException e) {
		
		e.printStackTrace();
		 return false;
	}
	
	}

	@Override
	public boolean updatePostComment(Comment comment) {
		try{
			sessionFactory.getCurrentSession().update(comment);
			return true;
		}catch(Exception e){
			System.out.println("Exception raised: "+e);
			return false;
		}
	}

	@Override
	public boolean deletePostComment(Comment comment) {
		try
		{
			sessionFactory.getCurrentSession().delete(comment);
			return true;
			
		}catch(Exception e){
			
			System.out.println("Exception raised: "+e);
			return false;
		}
	}

	@Override
	public Comment getPostComment(int blogCommentId) {
  Session session = sessionFactory.openSession();
		
		Comment blogObj = session.get(Comment.class, blogCommentId);
		
		session.close();
		
		return blogObj;
	}

	@Override
	public List<Comment> getAllPostComments(int postid) {
Session session = sessionFactory.openSession();
		
		List<Comment> blogCommentList= session.createQuery("from Comment where blogId = :blogId",Comment.class)
				.setParameter("blogId", postid).list();
		
		session.close();
		
		return blogCommentList;
		
	}

}
