package com.Customer_Feedback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Customer_Feedback.entity.AppUser;

public interface userRepo extends JpaRepository<AppUser, Long>{
	
	Optional<AppUser> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

}
