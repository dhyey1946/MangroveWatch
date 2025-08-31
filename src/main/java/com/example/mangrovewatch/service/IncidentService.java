package com.example.mangrovewatch.service;

import com.example.mangrovewatch.dto.IncidentRequest;
import com.example.mangrovewatch.dto.IncidentResponse;
import com.example.mangrovewatch.model.Incident;
import com.example.mangrovewatch.model.User;
import com.example.mangrovewatch.repository.IncidentRepository;
import com.example.mangrovewatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public IncidentResponse createIncident(IncidentRequest request, Long reporterId) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Incident incident = new Incident();
        incident.setLatitude(request.getLatitude());
        incident.setLongitude(request.getLongitude());
        incident.setIncidentType(request.getIncidentType());
        incident.setDescription(request.getDescription());
        incident.setLocationName(request.getLocationName());
        incident.setReporter(reporter);

        Incident savedIncident = incidentRepository.save(incident);
        return mapToResponse(savedIncident);
    }

    public List<IncidentResponse> getAllIncidents() {
        return incidentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<IncidentResponse> getIncidentsByReporter(Long reporterId) {
        return incidentRepository.findByReporterId(reporterId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Updated mapping method to handle all DTO fields properly
    private IncidentResponse mapToResponse(Incident incident) {
        IncidentResponse response = new IncidentResponse();
        response.setId(incident.getId());
        response.setLatitude(incident.getLatitude());
        response.setLongitude(incident.getLongitude());
        response.setIncidentType(incident.getIncidentType());
        response.setDescription(incident.getDescription());
        response.setLocationName(incident.getLocationName());
        response.setStatus(incident.getStatus() != null ? incident.getStatus().name() : null);
        response.setReportedAt(incident.getReportedAt());
        response.setValidatedAt(incident.getValidatedAt());

        // ✅ Map reporter to UserSummary
        if (incident.getReporter() != null) {
            User reporter = incident.getReporter();
            IncidentResponse.UserSummary userSummary = new IncidentResponse.UserSummary();
            userSummary.setId(reporter.getId());
            userSummary.setUsername(reporter.getUsername());
            userSummary.setFullName(reporter.getFullName());
            response.setReporter(userSummary);
        }

        // ✅ Map photos to PhotoResponse list
        if (incident.getPhotos() != null && !incident.getPhotos().isEmpty()) {
            List<IncidentResponse.PhotoResponse> photoResponses = incident.getPhotos().stream()
                    .map(photo -> {
                        IncidentResponse.PhotoResponse photoResponse = new IncidentResponse.PhotoResponse();
                        photoResponse.setId(photo.getId());
                        photoResponse.setUrl(photo.getUrl());
                        photoResponse.setFilename(photo.getFilename());
                        return photoResponse;
                    })
                    .collect(Collectors.toList());
            response.setPhotos(photoResponses);
        }

        return response;
    }
}
