package com.example.ToDoList.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDto) {
        String response = userService.registerUser(userDto);
        return ResponseEntity.ok(response);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO request) {
        User user = userRepository.findByUserId(request.getUserId()).orElse(null);

        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // 로그인 성공 시 userId와 username 반환
        return ResponseEntity.ok(new UserDTO(user.getUserId(),null, user.getUsername()));
    }
}
