CREATE TABLE door_device (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    house_id        BIGINT NOT NULL,
    device_code     VARCHAR(64) NOT NULL UNIQUE,   -- e.g. ESP32 MAC or provisioned ID
    device_name     VARCHAR(100) NOT NULL DEFAULT 'Front Door',
    device_secret   VARCHAR(255) NOT NULL,          -- shared secret the ESP32 sends with each event
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    last_seen_at    DATETIME NULL,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_device_house FOREIGN KEY (house_id) REFERENCES house(id) ON DELETE CASCADE,
    INDEX idx_device_house_id (house_id),
    INDEX idx_device_code (device_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
