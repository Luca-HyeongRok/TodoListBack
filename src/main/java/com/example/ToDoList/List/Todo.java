package com.example.ToDoList.List;

import com.example.ToDoList.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@Entity
@Table(name = "list_tb")  // DB 테이블과 매핑
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer listId;

    private String content;
    private int priority;
    private Timestamp startDate;
    private Timestamp endDate;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
