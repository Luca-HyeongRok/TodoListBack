package com.example.ToDoList.user;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final HttpSession session;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserDTO userDto) {
        String response = userService.registerUser(userDto);
        return ResponseEntity.ok(Map.of("message", response)); // JSON 형식 반환
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        User user = userRepository.findByUserId(userDTO.getUserId()).orElse(null);

        if (user == null || !user.getPassword().equals(userDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // 세션에 **실제 로그인된 사용자 정보** 저장 (DTO가 아니라 User 객체 저장)
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        // 로그인 성공 시 userId와 username 반환
        return ResponseEntity.ok(new UserDTO(user.getUserId(), null, user.getUsername()));
    }
    // 세션을 통해 로그인된 유저 정보
    @GetMapping("/session")
    public ResponseEntity<?> getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        User user = (User) session.getAttribute("user");
        return ResponseEntity.ok(new UserDTO(user.getUserId(), null, user.getUsername()));
    }
}
