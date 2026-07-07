package com.swp391.OnlineEnglishLearningSystem.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SliderDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer orderNumber;
    private String status;
    private String imageUrl;
    private String linkUrl;
}
