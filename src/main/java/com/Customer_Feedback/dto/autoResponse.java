package com.Customer_Feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class autoResponse {
	
	String token;
    String tokenType;
    String name;
    String email;
    String role;

}
