package com.example.ToDoList.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    // 기본적으로 findAll(), save(), delete() 등의 메서드 제공
    // 추가적인 쿼리 메서드를 정의할 수 있다.
    @Query("SELECT t FROM Todo t WHERE t.user.userId = :userId")
    List<Todo>  findByUser_UserId(@Param("userId") String userId);

    // 날짜 변경시 리스트 목록
    @Query(value = "SELECT * FROM LIST_TB t WHERE t.user_id = :userId AND t.start_date <= :endDate AND t.end_date >= :startDate",
            nativeQuery = true)
    List<Todo> findByUserIdAndDate(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 검색 정보 불러오기
    List<Todo> findByContentContainingIgnoreCase(String keyword);

}


