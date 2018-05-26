package org.collaborative.service;

import java.util.List;

import org.collaborative.model.User;

public interface UserService {

	public boolean saveUser(User userDetail);
	public List<User>userList();
	public boolean isEmailValid(String email);
	public User updateUser(User user);
	public User getUserById(long id);
	public User login(User user);
}
