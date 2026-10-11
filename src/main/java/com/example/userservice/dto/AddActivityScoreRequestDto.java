package com.example.userservice.dto;

public class AddActivityScoreRequestDto {
    private Long id;
    private int score;

    public AddActivityScoreRequestDto(Long id, int score) {
        this.id = id;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }
}
