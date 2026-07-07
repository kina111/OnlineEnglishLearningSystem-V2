package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    public OrderFilter(String status, Double minAmount, Double maxAmount, LocalDateTime startDate,
                       LocalDateTime endDate, LocalDateTime startUpdate, LocalDateTime endUpdate,
                       String sortBy, String sortDir, String search) {
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
}
