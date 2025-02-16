package com.example.ToDoList.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service // 비즈니스 로직 담당
@RequiredArgsConstructor // final 필드를 자동으로 생성자 주입해주는 Lombok
public class UserService {

    private final UserRepository userRepository; // DB와 상호작용하는 UserRepository
    //private final BCryptPasswordEncoder passwordEncoder; // 비밀번호를 암호화하기 위해 사용

    // 회원가입
    @Transactional
    public String registerUser(UserDTO userDto) {
        // userId 체크
        Optional<User> existingUser = userRepository.findByUserId(userDto.getUserId());

        if (existingUser.isPresent()) {
            return "이미 존재하는 아이디입니다."; // 중복 아이디가 존재하면 회원가입 불가능
        }

        // 없으면 회원가입 (비밀번호는 암호화)
        User newUser = new User(
                userDto.getUserId(), // 사용자가 입력한 userId
                userDto.getUsername(), // 사용자가 입력한 username
                userDto.getPassword()
                //passwordEncoder.encode(userDto.getPassword()) // 비밀번호를 암호화하여 저장
        );

        // DB저장
        userRepository.save(newUser);

        return "회원가입 성공"; // 회원가입 완료 메시지 반환
    }
}
