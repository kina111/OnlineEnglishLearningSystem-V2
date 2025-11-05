package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Order;

import java.time.LocalDateTime;

public class OrderFilter {
    private String status;
    private Double minAmount;
    private Double maxAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime startUpdate;
    private LocalDateTime endUpdate;
    private String sortBy; // amount, createdAt, updatedAt
    private String sortDir; // asc, desc
    private String search;

    public OrderFilter() {
    }

    public OrderFilter(String status, Double minAmount, Double maxAmount, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime startUpdate, LocalDateTime endUpdate, String sortBy, String sortDir, String search) {
        this.status = status;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startUpdate = startUpdate;
        this.endUpdate = endUpdate;
        this.sortBy = sortBy;
        this.sortDir = sortDir;
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartUpdate() {
        return startUpdate;
    }

    public void setStartUpdate(LocalDateTime startUpdate) {
        this.startUpdate = startUpdate;
    }

    public LocalDateTime getEndUpdate() {
        return endUpdate;
    }

    public void setEndUpdate(LocalDateTime endUpdate) {
        this.endUpdate = endUpdate;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
}
