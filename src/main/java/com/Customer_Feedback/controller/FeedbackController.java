
package com.Customer_Feedback.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Customer_Feedback.entity.AppUser;
import com.Customer_Feedback.entity.Feedback;
import com.Customer_Feedback.service.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Create feedback
    @PostMapping
    public ResponseEntity<?> createFeedback(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {

        AppUser user = (AppUser) authentication.getPrincipal();

        try {
            String subject = (String) request.get("subject");
            String message = (String) request.get("message");
            int rating = Integer.parseInt(
                    String.valueOf(request.get("rating")));

            if (subject == null || subject.isBlank()
                    || message == null || message.isBlank()
                    || rating < 1 || rating > 5) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message",
                                "Subject, message and rating (1-5) are required"));
            }

            Feedback feedback = feedbackService.createFeedback(
                    user, subject, message, rating);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Feedback created successfully",
                            "feedbackId", feedback.getId(),
                            "subject", feedback.getSubject(),
                            "rating", feedback.getRating()
                    ));

        } catch (NumberFormatException | ClassCastException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Rating must be a number from 1 to 5"));
        }
    }

    // Get logged-in user's feedback
    @GetMapping("/my")
    public ResponseEntity<List<Feedback>> getMyFeedback(
            Authentication authentication) {

        AppUser user = (AppUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                feedbackService.getMyFeedback(user));
    }

    // Update logged-in user's own feedback
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMyFeedback(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request,
            Authentication authentication) {

        AppUser user = (AppUser) authentication.getPrincipal();

        try {
            String subject = (String) request.get("subject");
            String message = (String) request.get("message");
            int rating = Integer.parseInt(
                    String.valueOf(request.get("rating")));

            if (subject == null || subject.isBlank()
                    || message == null || message.isBlank()
                    || rating < 1 || rating > 5) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message",
                                "Subject, message and rating (1-5) are required"));
            }

            Feedback feedback = feedbackService.updateMyFeedback(
                    id, user, subject, message, rating);

            return ResponseEntity.ok(Map.of(
                    "message", "Feedback updated successfully",
                    "feedbackId", feedback.getId(),
                    "subject", feedback.getSubject(),
                    "rating", feedback.getRating()
            ));

        } catch (NumberFormatException | ClassCastException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Rating must be a number from 1 to 5"));
        }
    }
}
