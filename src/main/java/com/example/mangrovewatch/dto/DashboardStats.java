package com.example.mangrovewatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardStats {
    private Long totalIncidents;
    private Long validatedReports;
    private Long activeUsers;
    private Long pendingReports;

    private Map<String, Long> incidentsByType;
    private Map<String, Long> incidentsByMonth;
    private List<TopContributor> topContributors;
}

