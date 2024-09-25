package com.enotes.Enotes.service;

import com.enotes.Enotes.entity.User;

public interface UserService {

	public User saveUser(User user);

	public boolean existEmailCheck(String email);
	
	//added

}