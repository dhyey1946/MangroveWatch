package com.example.mangrovewatch.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "incidents")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Incident {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private Double latitude;
    @Column(nullable = false) private Double longitude;
    @Column(nullable = false) private String incidentType;
    @Column(nullable = false, length = 1000) private String description;
    private String locationName;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime reportedAt = LocalDateTime.now();
    private LocalDateTime validatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private User reporter;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<Photo> photos;

    public enum Status { PENDING, VALIDATED, REJECTED }
}
