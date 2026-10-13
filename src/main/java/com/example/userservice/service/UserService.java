package com.example.userservice.service;

import com.example.userservice.client.PointClient;
import com.example.userservice.domain.User;
import com.example.userservice.dto.AddActivityScoreRequestDto;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.event.UserSignedUpEvent;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PointClient pointClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserService(UserRepository userRepository, PointClient pointClient, KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.pointClient = pointClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        User user = new User(
                signUpRequestDto.getEmail(),
                signUpRequestDto.getName(),
                signUpRequestDto.getPassword()
        );
        User savedUser = userRepository.save(user);

        pointClient.addPoint(savedUser.getId(), 1000);

        UserSignedUpEvent userSignedUpEvent = new UserSignedUpEvent(savedUser.getId(), savedUser.getName());
        kafkaTemplate.send("user.signed-up", toJsonString(userSignedUpEvent));
    }

    private String toJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JacksonException e) {
            throw new RuntimeException("Json 직렬화 실패");
        }
    }

    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public List<UserResponseDto> getUsersByIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);

        return users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getEmail(),
                        user.getName()
                ))
                .toList();
    }

    @Transactional
    public void addActivityScore(AddActivityScoreRequestDto addActivityScoreRequestDto) {
        User user = userRepository.findById(addActivityScoreRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.addActivityScore(addActivityScoreRequestDto.getScore());

        userRepository.save(user);
    }
}
