package com.example.ToDoList.List;

import com.example.ToDoList.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Todo updateTodo(@PathVariable Integer listId, @RequestBody Todo updatedTodo) {
        System.out.println("Controller 111111111111111111111111111111");
        return todoService.updateTodo(listId, updatedTodo);
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
}
