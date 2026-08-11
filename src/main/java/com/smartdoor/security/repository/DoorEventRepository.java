package com.smartdoor.security.repository;

import com.smartdoor.security.entity.DoorEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoorEventRepository extends JpaRepository<DoorEvent, Long> {

    Optional<DoorEvent> findTopByDeviceIdOrderByEventTimeDesc(Long deviceId);

    Optional<DoorEvent> findTopByDeviceIdAndStatusOrderByEventTimeDesc(
            Long deviceId, DoorEvent.DoorStatus status);

    List<DoorEvent> findByDeviceIdAndEventTimeBetweenOrderByEventTimeDesc(
            Long deviceId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(e) FROM DoorEvent e WHERE e.device.id = :deviceId " +
           "AND e.status = :status AND e.eventTime BETWEEN :start AND :end")
    Long countByDeviceIdAndStatusAndEventTimeBetween(
            @Param("deviceId") Long deviceId,
            @Param("status") DoorEvent.DoorStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<DoorEvent> findByDeviceIdOrderByEventTimeDesc(Long deviceId);
}
