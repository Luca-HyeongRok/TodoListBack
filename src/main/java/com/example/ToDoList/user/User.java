package com.example.ToDoList.user;

import com.example.ToDoList.List.Todo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos; // 유저가 가진 할 일 목록 (1:N 관계)

    @Builder
    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
