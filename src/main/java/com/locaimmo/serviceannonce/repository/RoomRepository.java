package com.locaimmo.serviceannonce.repository;

import com.locaimmo.serviceannonce.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}