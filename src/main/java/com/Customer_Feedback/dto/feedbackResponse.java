
package com.Customer_Feedback.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class feedbackResponse {

    private Long id;
    private String subject;
    private String message;
    private Integer rating;

    private Long userId;
    private String userName;
    private String userEmail;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
