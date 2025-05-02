package com.arben.jxaadf.tender;

import java.util.List;
import java.util.ArrayList;

public class Tender {
    private int tenderId; // DO NOT SEND
    private String title;
    private String description;
    private String status; // Active / Ended
    private String authorId; // ID of the user who created the tender
    private String createdDate;
    private String deadline;
    private String budget;
    private List<String> documentLinks;

    public Tender() {
        this.documentLinks = new ArrayList<>();
    }

    public Tender(String title, String description, String status, String authorId,
            String createdDate, String deadline, String budget) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.authorId = authorId;
        this.createdDate = createdDate;
        this.deadline = deadline;
        this.budget = budget;
        this.documentLinks = new ArrayList<>();
    }

    public Tender(int tenderId, String title, String description, String status, String authorId,
            String createdDate, String deadline, String budget) {
        this.tenderId = tenderId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.authorId = authorId;
        this.createdDate = createdDate;
        this.deadline = deadline;
        this.budget = budget;
        this.documentLinks = new ArrayList<>();
    }

    public Tender(int tenderId, String title, String description, String status, String authorId,
            String createdDate, String deadline, String budget, List<String> documentLinks) {
        this.tenderId = tenderId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.authorId = authorId;
        this.createdDate = createdDate;
        this.deadline = deadline;
        this.budget = budget;
        this.documentLinks = documentLinks != null ? documentLinks : new ArrayList<>();
    }

    public int getTenderId() {
        return tenderId;
    }

    public void setTenderId(int tenderId) {
        this.tenderId = tenderId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public List<String> getDocumentLinks() {
        return documentLinks;
    }

    public void setDocumentLinks(List<String> documentLinks) {
        this.documentLinks = documentLinks != null ? documentLinks : new ArrayList<>();
    }

    public void addDocumentLink(String documentLink) {
        if (this.documentLinks == null) {
            this.documentLinks = new ArrayList<>();
        }
        if (documentLink != null && !documentLink.isEmpty()) {
            this.documentLinks.add(documentLink);
        }
    }

    public void removeDocumentLink(String documentLink) {
        if (this.documentLinks != null) {
            this.documentLinks.remove(documentLink);
        }
    }

}
