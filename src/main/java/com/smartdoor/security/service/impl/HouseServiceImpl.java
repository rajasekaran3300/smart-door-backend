package com.smartdoor.security.service.impl;

import com.smartdoor.security.entity.House;
import com.smartdoor.security.entity.User;
import com.smartdoor.security.exception.ResourceNotFoundException;
import com.smartdoor.security.repository.HouseRepository;
import com.smartdoor.security.repository.UserRepository;
import com.smartdoor.security.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    @Override
    public House getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return houseRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("House not found for user"));
    }
}
