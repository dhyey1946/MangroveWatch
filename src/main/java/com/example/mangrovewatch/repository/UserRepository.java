package com.example.mangrovewatch.repository;
import com.example.mangrovewatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    @Query("SELECT u FROM User u ORDER BY u.points DESC, u.totalReports DESC")
    List<User> findTopContributors();

    Long countByActiveTrue();
}

