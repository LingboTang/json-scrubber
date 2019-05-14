package com.example.demo;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class JsonInfo {

    private UUID uuid;
    private ZonedDateTime logDate;

    public JsonInfo() {
        this.uuid = UUID.randomUUID();
        this.logDate = ZonedDateTime.now();
    }

    public UUID getUUID() {
        return uuid;
    }

    public ZonedDateTime getLogDate() {
        return logDate;
    }

}
