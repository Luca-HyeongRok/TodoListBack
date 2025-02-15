package com.example.ToDoList.List;

import com.example.ToDoList.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 로그인한 사용자의 Todo 목록만 가져오기
    // GET 대신 POST를 사용하는 이유: 비밀번호 유출 방지
    @PostMapping("/user")
    public List<Todo> getTodos(@RequestBody User user) {
        return todoService.findTodosByUserId(user.getUserId());
    }

    // listId Todo 항목을 가져오기 (GET)
    @GetMapping("/{listId}")
    public Todo getTodoByListId(@PathVariable Integer listId) {
        return todoService.getTodoById(listId);  // listId를 이용해 특정 Todo 항목을 반환
    }

    // 체크박스 상태 변경 (isDone 필드 업데이트) (PATCH)
    @PatchMapping("/{listId}")
    public Todo updateTodoCheck(@PathVariable Integer listId, @RequestBody Todo updatedTodo) {
        System.out.println("TodoController: 업데이트 요청받음. Todo ID: " + listId);
        return todoService.updateTodoCheckbox(listId, updatedTodo);
    }

    // Todo 내용, 중요도, 날짜 변경 (PATCH)
    @PatchMapping("/edit/{listId}")
    public Todo updateTodo(@PathVariable Integer listId, @RequestBody Todo updatedTodo) {
        System.out.println("Todo 수정 요청: ID = " + listId);
        return todoService.updateTodo(listId, updatedTodo);
    }

    // listId로 Todo 삭제 (DELETE)
    @DeleteMapping("/{listId}")
    public ResponseEntity<String> deleteTodo(@PathVariable Integer listId) {
        System.out.println("삭제 요청 받은 Todo ID: " + listId);
        todoService.deleteTodo(listId);  // 서비스 레이어에서 실제 삭제 처리
        return ResponseEntity.ok("삭제 완료");
    }

    // 새로운 Todo 생성 (POST)
    @PostMapping
    public Todo createTodo(@RequestBody Todo newTodo) {
        return todoService.createTodo(newTodo);  // 서비스 레이어에 위임하여 Todo 생성
    }
}
