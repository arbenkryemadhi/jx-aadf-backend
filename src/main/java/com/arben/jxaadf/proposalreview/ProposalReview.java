package com.arben.jxaadf.proposalreview;

public class ProposalReview {
    private int proposalReviewId; // DO NOT SEND
    private int proposalId;
    private String authorId;
    private String title;
    private String description;
    private String createdDate;

    public ProposalReview() {}

    public ProposalReview(int proposalReviewId, int proposalId, String authorId, String title,
            String description, String createdDate) {
        this.proposalReviewId = proposalReviewId;
        this.proposalId = proposalId;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
    }

    public int getProposalReviewId() {
        return proposalReviewId;
    }

    public void setProposalReviewId(int proposalReviewId) {
        this.proposalReviewId = proposalReviewId;
    }

    public int getProposalId() {
        return proposalId;
    }

    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
