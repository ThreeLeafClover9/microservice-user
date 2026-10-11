package com.example.userservice.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class BoardCreatedEvent {
    private Long userId;

    public BoardCreatedEvent() {
    }

    public static BoardCreatedEvent fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, BoardCreatedEvent.class);
        } catch (JacksonException e) {
            throw new RuntimeException("Json 파싱 실패");
        }
    }

    public Long getUserId() {
        return userId;
    }
}
