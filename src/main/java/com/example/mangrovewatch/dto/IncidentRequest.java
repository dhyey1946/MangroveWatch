package com.example.mangrovewatch.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class IncidentRequest {
    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotBlank
    private String incidentType;

    @NotBlank
    private String description;

    private String locationName;
    private List<String> photoUrls;
}

