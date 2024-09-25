package com.enotes.Enotes.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.Enotes.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public boolean existsByEmail(String email);

	public User findByEmail(String email);

}
