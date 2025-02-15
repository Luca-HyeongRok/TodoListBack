package com.example.ToDoList.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String password;
    private String username;


    public UserDTO(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
