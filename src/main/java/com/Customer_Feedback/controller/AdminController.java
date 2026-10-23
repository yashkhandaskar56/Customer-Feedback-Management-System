
package com.Customer_Feedback.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Customer_Feedback.dto.feedbackResponse;
import com.Customer_Feedback.entity.Feedback;
import com.Customer_Feedback.service.FeedbackService;

@RestController
@RequestMapping("/api/admin/feedback")
public class AdminController {

    private final FeedbackService feedbackService;

    public AdminController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // 1. Get all feedback
    @GetMapping
    public ResponseEntity<List<feedbackResponse>> getAllFeedback() {

        List<feedbackResponse> responses =
                feedbackService.getAllFeedback()
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    // 2. Get feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<feedbackResponse> getFeedbackById(
            @PathVariable Long id) {

        Feedback feedback = feedbackService.getFeedbackById(id);

        return ResponseEntity.ok(convertToResponse(feedback));
    }

    // 3. Update feedback
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedback(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        Object subjectValue = request.get("subject");
        Object messageValue = request.get("message");
        Object ratingValue = request.get("rating");

        if (!(subjectValue instanceof String subject)
                || subject.isBlank()
                || !(messageValue instanceof String message)
                || message.isBlank()
                || ratingValue == null) {

            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "Subject, message and rating are required"));
        }

        int rating;

        try {
            rating = Integer.parseInt(String.valueOf(ratingValue));
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "Rating must be a number from 1 to 5"));
        }

        if (rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "Rating must be between 1 and 5"));
        }

        Feedback updatedFeedback =
                feedbackService.adminUpdateFeedback(
                        id, subject, message, rating);

        return ResponseEntity.ok(
                convertToResponse(updatedFeedback));
    }

    // 4. Delete feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFeedback(
            @PathVariable Long id) {

        feedbackService.deleteFeedback(id);

        return ResponseEntity.ok(
                Map.of("message", "Feedback deleted successfully"));
    }

    // Convert Feedback entity to feedbackResponse DTO
    private feedbackResponse convertToResponse(Feedback feedback) {

        feedbackResponse response = new feedbackResponse();

        response.setId(feedback.getId());
        response.setSubject(feedback.getSubject());
        response.setMessage(feedback.getMessage());
        response.setRating(feedback.getRating());

        if (feedback.getUser() != null) {
            response.setUserId(feedback.getUser().getId());
            response.setUserName(feedback.getUser().getName());
            response.setUserEmail(feedback.getUser().getEmail());
        }

        response.setCreatedAt(feedback.getCreatedAt());
        response.setUpdatedAt(feedback.getUpdatedAt());

        return response;
    }
}
