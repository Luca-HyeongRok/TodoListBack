package com.example.ToDoList.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    // 기본적으로 findAll(), save(), delete() 등의 메서드 제공
    // 추가적인 쿼리 메서드를 정의할 수 있다.
    @Query("SELECT t FROM Todo t WHERE t.user.userId = :userId")
    List<Todo> findByUserId(@Param("userId") String userId);

}
