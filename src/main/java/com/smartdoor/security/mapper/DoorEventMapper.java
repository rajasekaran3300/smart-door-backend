package com.smartdoor.security.mapper;

import com.smartdoor.security.dto.response.DoorHistoryResponse;
import com.smartdoor.security.entity.DoorEvent;
import org.springframework.stereotype.Component;

@Component
public class DoorEventMapper {

    public DoorHistoryResponse toHistoryResponse(DoorEvent event) {
        return DoorHistoryResponse.builder()
                .id(event.getId())
                .status(event.getStatus().name())
                .eventTime(event.getEventTime())
                .build();
    }
}
