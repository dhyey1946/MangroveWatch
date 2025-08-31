package com.example.mangrovewatch.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidentResponse {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String incidentType;
    private String description;
    private String locationName;
    private String status;
    private String severity;
    private LocalDateTime reportedAt;
    private LocalDateTime validatedAt;
    private UserSummary reporter;
    private List<PhotoResponse> photos;

    // ✅ Inner classes for nested objects
    @Data
    public static class UserSummary {
        private Long id;
        private String username;
        private String fullName;
    }

    @Data
    public static class PhotoResponse {
        private Long id;
        private String url;
        private String filename;
    }
}
