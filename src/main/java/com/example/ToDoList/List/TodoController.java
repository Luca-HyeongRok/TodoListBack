package com.example.ToDoList.List;

import com.example.ToDoList.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;
    private final TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoService todoService, TodoRepository todoRepository) {
        this.todoService = todoService;
        this.todoRepository = todoRepository;
    }

    // 로그인한 사용자의 Todo 목록만 가져오기
    @PostMapping("/user")
    public List<Todo> getTodos(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId가 필요합니다.");
        }

        return todoService.findTodosByUserId(userId);
    }

    // 특정 Todo 가져오기
    @GetMapping("/{listId}")
    public Todo getTodoByListId(@PathVariable Integer listId) {
        return todoService.getTodoById(listId);
    }

    // 체크박스 상태 변경
    @PatchMapping("/{listId}")
    public Todo updateTodoCheck(@PathVariable Integer listId, @RequestBody Todo updatedTodo) {
        return todoService.updateTodoCheckbox(listId, updatedTodo);
    }

    // Todo 내용, 중요도, 날짜 변경
    @PatchMapping("/edit/{listId}")
    public ResponseEntity<?> updateTodo(HttpServletRequest request, @PathVariable Integer listId, @RequestBody Todo updatedTodo) {
        HttpSession session = request.getSession(false); // 세션 없으면 null 반환
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        System.out.println("컨트롤러: 업데이트 요청 받음");

        try {
            Todo savedTodo = todoService.updateTodo(listId, updatedTodo);
            return ResponseEntity.ok(savedTodo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Todo 삭제
    @DeleteMapping("/{listId}")
    public ResponseEntity<String> deleteTodo(@PathVariable Integer listId) {
        todoService.deleteTodo(listId);
        return ResponseEntity.ok("삭제 완료");
    }

    // 새로운 Todo 생성
    @PostMapping
    public ResponseEntity<?> createTodo(HttpServletRequest request, @RequestBody Todo newTodo) {

        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        newTodo.setUser(loggedInUser); // 세션에서 가져온 userId 설정

        Todo savedTodo = todoService.createTodo(newTodo);
        return ResponseEntity.ok(savedTodo);
    }
    // 날짜 변경시 리스트 목록
    @GetMapping("/date")
    public ResponseEntity<?> getTodosByDate(@RequestParam String date, HttpServletRequest request) {
        try {
            // 세션 확인
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            User user = (User) session.getAttribute("user");

            //  날짜 변환 (String -> LocalDateTime, 00:00:00 기본값 추가)
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;
            try {
                LocalDate selectedDate = LocalDate.parse(date);
                startDateTime = selectedDate.atStartOfDay(); // 2025-02-16 00:00:00
                endDateTime = selectedDate.atTime(23, 59, 59); // 2025-02-16 23:59:59
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("잘못된 날짜 형식입니다. YYYY-MM-DD 형식이어야 합니다.");
            }

            // 데이터 조회 (LocalDateTime 사용)
            List<Todo> todos = todoService.findTodosByDate(user.getUserId(), startDateTime, endDateTime);
            return ResponseEntity.ok(todos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 내부 오류 발생: " + e.getMessage());
        }
    }
    // 검색 정보 불러오기
    @GetMapping("/search")
    public ResponseEntity<List<Todo>> searchTodos(@RequestParam(name = "keyword", required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<Todo> results = todoService.searchTodos(keyword);
        return ResponseEntity.ok(results);
    }

}
