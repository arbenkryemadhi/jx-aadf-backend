package com.arben.jxaadf.proposal;

public class Proposal {
    private int proposalId; // DO NOT SEND
    private int tenderId; // ID of the tender this proposal is associated with
    private String authorId; // ID of the user who created the proposal
    private String title;
    private String description;
    private String price;
    private String status; // Accepted / Pending / Rejected
    private String createdDate;


    public Proposal() {}

    public Proposal(int tenderId, String authorId, String title, String description, String price,
            String status, String createdDate) {
        this.tenderId = tenderId;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.createdDate = createdDate;
    }

    public Proposal(int proposalId, int tenderId, String authorId, String title, String description,
            String price, String status, String createdDate) {
        this.proposalId = proposalId;
        this.tenderId = tenderId;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.createdDate = createdDate;
    }

    public int getProposalId() {
        return proposalId;
    }

    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }

    public int getTenderId() {
        return tenderId;
    }

    public void setTenderId(int tenderId) {
        this.tenderId = tenderId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
