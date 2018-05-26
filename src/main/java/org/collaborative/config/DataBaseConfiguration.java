package org.collaborative.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.collaborative.dao.UserDAO;
import org.collaborative.dao.UserDAOImpl;
import org.collaborative.model.*;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public class DataBaseConfiguration {
	
	
Logger logger =LoggerFactory.getLogger(DataBaseConfiguration.class);
	
	@Bean(name = "dataSource")
		public DataSource getDataSource() {
		logger.info("Data Source Configuration ");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:XE");
		dataSource.setUsername("collab_user");
		dataSource.setPassword("1234");
		logger.info("Data Base Connected ");
		return dataSource;

	}

	private Properties getHibernateProperties() {
		logger.info("========Hibernate Properties=========== ");
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.format_sql","true");
		logger.info("========Hibernate Properties  has been set=========== ");
		return properties;

	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		logger.info("========Hibernate Session Factory=========== ");
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.addProperties(getHibernateProperties());
		sessionBuilder.addAnnotatedClasses(User.class);
		sessionBuilder.addAnnotatedClasses(Post.class);
		sessionBuilder.addAnnotatedClass(Notification.class);
		sessionBuilder.addAnnotatedClass(PostLikes.class);
		sessionBuilder.addAnnotatedClass(Comment.class);
	
		logger.info("========Hibernate SessionFactory Object created=========== ");
		return sessionBuilder.buildSessionFactory();

	}

	@Autowired
	@Bean(name = "transactionManager")
 	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		logger.info("========Hibernate Transaction =========== ");
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		logger.info("========Hibernate Transaction object created=========== ");
		return transactionManager;
	}
	
	@Autowired
	@Bean(name = "userDAO")
	public UserDAO getUserDao(SessionFactory sessionFactory) {
	    return new UserDAOImpl(sessionFactory);
	}
	
	
		@Bean
    	public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //Using gmail       
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("s.preetti27@gmail.com");
        mailSender.setPassword("lovesine");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }
	
	
	
}