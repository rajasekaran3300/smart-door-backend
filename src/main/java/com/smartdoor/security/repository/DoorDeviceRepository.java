package com.smartdoor.security.repository;

import com.smartdoor.security.entity.DoorDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface DoorDeviceRepository extends JpaRepository<DoorDevice, Long> {
    Optional<DoorDevice> findByDeviceCode(String deviceCode);
    List<DoorDevice> findByHouseId(Long houseId);
}
