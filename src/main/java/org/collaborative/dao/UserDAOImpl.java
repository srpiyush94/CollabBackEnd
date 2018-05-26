package org.collaborative.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import org.collaborative.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("deprecation")
@Repository
public class UserDAOImpl implements UserDAO {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public UserDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	@Autowired
	SessionFactory sessionFactory;

	@Transactional
	public boolean saveUser(User userDetail) {
		LOGGER.info("Registration start");
		userDetail.setEnabled(false);
		userDetail.setIssuedDateTime(LocalDateTime.now());
		userDetail.setStatus(User.STATUS_PENDING);
		userDetail.setRole("ROLE_USER");
		sessionFactory.getCurrentSession().saveOrUpdate(userDetail);
		LOGGER.info("Registration has been complete successfully");
		return true;

	}

	@Transactional
	public List<User> userList() {
		// Open a session
		Session session = sessionFactory.openSession();

		// DEPRECATED: Create Criteria
		// Criteria criteria = session.createCriteria(BlogUserDetail.class);

		// DEPRECATED: Get a list of Contact objects according to the Criteria
		// object
		// List<BlogUserDetail> contacts = criteria.list();

		// UPDATED: Create CriteriaBuilder
		CriteriaBuilder builder = session.getCriteriaBuilder();

		// UPDATED: Create CriteriaQuery
		CriteriaQuery<User> criteria = builder.createQuery(User.class);

		// UPDATED: Specify criteria root
		criteria.from(User.class);

		// UPDATED: Execute query
		List<User> users = session.createQuery(criteria).getResultList();

		// Close the session
		session.close();

		return users;
	}

	public boolean isEmailValid(String email) {
		List<User> list = userList();

		for (User usersDetail : list) {
			if (usersDetail.getEmail().equals(email)) {
				return false;// invalid user
			}
		}
		return true;// valid user
	}

	@Transactional
	public User updateUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.update(user);
		return user;
	}

	@Transactional
	public User getUserById(long id) {
		Session session = sessionFactory.getCurrentSession();
		User userdt = (User) session.get(User.class, id);
		return userdt;

	}

	
	@SuppressWarnings("rawtypes")
	@Transactional
	public User login(User user) {
	
			Session session=sessionFactory.getCurrentSession();
			Query query=session.createQuery("from User Where email=? and password=?");
			query.setString(0,user.getEmail());
			query.setString(1,user.getPassword());
			
			return (User)query.uniqueResult();
		}
//select email,password from user where email='sr.piyush94@gmail.com' and password='1234';
}
