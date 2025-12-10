package com.locaimmo.serviceannonce.repository;

import com.locaimmo.serviceannonce.domain.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    
    @Query("SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.rooms")
    List<Property> findAllWithRooms();
    
    @Query("SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.rooms WHERE p.id = :id")
    Optional<Property> findByIdWithRooms(Long id);
}
