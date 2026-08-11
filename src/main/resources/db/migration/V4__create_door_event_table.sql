CREATE TABLE door_event (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id       BIGINT NOT NULL,
    status          ENUM('OPEN','CLOSED') NOT NULL,
    event_time      DATETIME NOT NULL,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_event_device FOREIGN KEY (device_id) REFERENCES door_device(id) ON DELETE CASCADE,
    INDEX idx_event_device_id (device_id),
    INDEX idx_event_time (event_time),
    INDEX idx_event_device_time (device_id, event_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
