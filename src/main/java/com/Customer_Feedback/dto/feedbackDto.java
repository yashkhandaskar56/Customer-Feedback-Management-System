package com.Customer_Feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class feedbackDto {
	
	String subject;
	String message;
	String rating;
	

}
