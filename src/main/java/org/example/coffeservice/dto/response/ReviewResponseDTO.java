package org.example.coffeservice.dto.response;

import java.util.Date;

public class ReviewResponseDTO {

    private Long id;
    private String userName;
    private int rating;
    private String reviewText;
    private Date reviewDate;

    public ReviewResponseDTO(Long id, String userName, int rating, String reviewText, Date reviewDate) {
        this.id = id;
        this.userName = userName;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
