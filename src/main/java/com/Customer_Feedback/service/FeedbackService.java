
package com.Customer_Feedback.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.Customer_Feedback.entity.AppUser;
import com.Customer_Feedback.entity.Feedback;
import com.Customer_Feedback.repository.FeedbackRepo;

@Service
public class FeedbackService {

    private final FeedbackRepo feedbackRepository;

    public FeedbackService(FeedbackRepo feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // Create new feedback
    public Feedback createFeedback(
            AppUser user,
            String subject,
            String message,
            int rating) {

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setSubject(subject);
        feedback.setMessage(message);
        feedback.setRating(rating);
        

        return feedbackRepository.save(feedback);
    }

    // Get feedback created by the logged-in user
    public List<Feedback> getMyFeedback(AppUser user) {
        return feedbackRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    // Update own feedback
    @Transactional
    public Feedback updateMyFeedback(
            Long feedbackId,
            AppUser user,
            String subject,
            String message,
            int rating) {

        Feedback feedback = findFeedbackById(feedbackId);

        if (!feedback.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can update only your own feedback");
        }

        feedback.setSubject(subject);
        feedback.setMessage(message);
        feedback.setRating(rating);

        return feedbackRepository.save(feedback);
    }

    // Admin: get all feedback
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    // Admin: get feedback by ID
    public Feedback getFeedbackById(Long feedbackId) {
        return findFeedbackById(feedbackId);
    }

    // Admin: update any feedback
    @Transactional
    public Feedback adminUpdateFeedback(
            Long feedbackId,
            String subject,
            String message,
            int rating) {

        Feedback feedback = findFeedbackById(feedbackId);

        feedback.setSubject(subject);
        feedback.setMessage(message);
        feedback.setRating(rating);

        return feedbackRepository.save(feedback);
    }

    // Admin: delete feedback
    public void deleteFeedback(Long feedbackId) {
        Feedback feedback = findFeedbackById(feedbackId);
        feedbackRepository.delete(feedback);
    }

    // Find feedback or return 404
    private Feedback findFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Feedback not found with ID: " + feedbackId));
    }
}