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
   // 로그인한 사용자의 Todo목록 조회
    @Query("SELECT t FROM Todo t WHERE t.user.userId = :userId ORDER BY t.priority ASC")
    List<Todo>  findByUser_UserId(@Param("userId") String userId);

    // 검색 정보 불러오기
    List<Todo> findByContentContainingIgnoreCase(String keyword);

    // 날짜 변경시 중요도 높은 순으로 정렬하여 해당 날짜 Todo 리스트 조회
    @Query("SELECT t FROM Todo t WHERE t.user.userId = :userId AND t.startDate <= :endDate AND t.endDate >= :startDate ORDER BY t.priority ASC")
    List<Todo> findByUserIdAndDate(
            @Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}


