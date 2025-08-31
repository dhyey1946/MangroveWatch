package com.example.mangrovewatch.controller;

import com.example.mangrovewatch.dto.IncidentRequest;
import com.example.mangrovewatch.dto.IncidentResponse;
import com.example.mangrovewatch.security.JwtUserDetails;
import com.example.mangrovewatch.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(
            @Valid @RequestBody IncidentRequest request,
            @AuthenticationPrincipal JwtUserDetails principal) {

        Long reporterId = principal.getId();
        IncidentResponse response = incidentService.createIncident(request, reporterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @GetMapping("/me")
    public ResponseEntity<List<IncidentResponse>> myIncidents(
            @AuthenticationPrincipal JwtUserDetails principal) {
        return ResponseEntity.ok(incidentService.getIncidentsByReporter(principal.getId()));
    }
}
