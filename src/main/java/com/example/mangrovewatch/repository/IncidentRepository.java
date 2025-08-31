package com.example.mangrovewatch.repository;
import com.example.mangrovewatch.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatus(Incident.Status status);
    List<Incident> findByIncidentType(String incidentType);
    List<Incident> findByReporterId(Long reporterId);

    @Query("SELECT i FROM Incident i WHERE i.reportedAt BETWEEN :startDate AND :endDate")
    List<Incident> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(i) FROM Incident i WHERE i.status = :status")
    Long countByStatus(@Param("status") Incident.Status status);

    @Query("SELECT i.incidentType, COUNT(i) FROM Incident i GROUP BY i.incidentType")
    List<Object[]> countByIncidentType();

    @Query("SELECT FUNCTION('MONTH', i.reportedAt), COUNT(i) FROM Incident i " +
            "WHERE i.reportedAt >= :startDate GROUP BY FUNCTION('MONTH', i.reportedAt)")
    List<Object[]> countByMonth(@Param("startDate") LocalDateTime startDate);
}
