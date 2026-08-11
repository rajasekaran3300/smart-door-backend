package com.smartdoor.security.repository;

import com.smartdoor.security.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {
    Optional<House> findByUserId(Long userId);
}
