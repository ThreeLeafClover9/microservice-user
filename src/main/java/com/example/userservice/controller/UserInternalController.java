package com.example.userservice.controller;

import com.example.userservice.dto.AddActivityScoreRequestDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
public class UserInternalController {
    private final UserService userService;

    public UserInternalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.getUser(id);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsersByIds(@RequestParam List<Long> ids) {
        List<UserResponseDto> userResponseDtos = userService.getUsersByIds(ids);
        return ResponseEntity.ok(userResponseDtos);
    }

    @PostMapping("/activity-score/add")
    public ResponseEntity<Void> addActivityScore(@RequestBody AddActivityScoreRequestDto addActivityScoreRequestDto) {
        userService.addActivityScore(addActivityScoreRequestDto);
        return ResponseEntity.noContent().build();
    }
}
