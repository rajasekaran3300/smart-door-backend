package com.smartdoor.security.service;

import com.smartdoor.security.entity.House;

public interface HouseService {
    House getByUsername(String username);
}
