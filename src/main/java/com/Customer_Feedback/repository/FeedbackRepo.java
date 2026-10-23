package com.Customer_Feedback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Customer_Feedback.entity.Feedback;

public interface FeedbackRepo extends JpaRepository<Feedback, Long>{
	
	List<Feedback> findByUserIdOrderByCreatedAtDesc(Long userId);

}
