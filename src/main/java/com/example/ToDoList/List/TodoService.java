package com.example.ToDoList.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    // 생성자 주입으로 TodoRepository를 주입받음
    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 로그인한 Todo 항목 반환
    public List<Todo> findTodosByUserId(String userId) {
        return todoRepository.findByUser_UserId(userId); // userId 기반으로 필터링
    }

    // listId를 통해 Todo 항목을 조회하는 메서드
    public Todo getTodoById(Integer listId) {
        //listId에 해당하는 Todo가 존재하는지 확인
       //System.out.println("Todo 조회 요청: ID = " + listId);  // 조회 요청 로그

        Optional<Todo> todoOptional = todoRepository.findById(listId);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            System.out.println("조회된 Todo: " + todo);  // 조회된 Todo 출력
            return todo;  // Todo가 존재하면 반환
        } else {
            System.out.println("Todo 항목을 찾을 수 없습니다. ID: " + listId);  // Todo가 없을 때 출력
            throw new RuntimeException("Todo 항목을 찾을 수 없습니다. ID: " + listId);  // 예외 처리
        }
    }

    // Todo 수정 메서드
    public Todo updateTodo(Integer listId, Todo updatedTodo) {
        Optional<Todo> optionalTodo = todoRepository.findById(listId);

        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();

            // 기존 데이터를 새 값으로 변경
            todo.setContent(updatedTodo.getContent());
            todo.setPriority(updatedTodo.getPriority());
            todo.setStartDate(updatedTodo.getStartDate());
            todo.setEndDate(updatedTodo.getEndDate());

            // DB에 저장 후 반환
            return todoRepository.save(todo);
        } else {
            throw new RuntimeException("수정할 Todo를 찾을 수 없습니다. ID: " + listId);
        }
    }

    // 체크박스 Todo 상태 업데이트
    public Todo updateTodoCheckbox(Integer listId, Todo updatedTodo) {
        // Todo 항목을 찾음
        Optional<Todo> optionalTodo = todoRepository.findById(listId);

        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();

            // 업데이트되는 'done' 값 로그로 확인
            System.out.println("Todo ID: " + listId + " - 상태 업데이트 전 done 값: " + todo.isDone() + " -> 상태 업데이트 후 done 값: " + updatedTodo.isDone());

            // done 값을 업데이트
            todo.setDone(updatedTodo.isDone());

            // DB에 업데이트된 Todo 저장
            Todo updatedTodoInDb = todoRepository.save(todo);

            // 업데이트 완료된 Todo 정보 로그로 확인
            System.out.println("Todo ID: " + listId + " - 상태가 성공적으로 업데이트되었습니다. 새로운 Todo: " + updatedTodoInDb);

            return updatedTodoInDb;  // 업데이트된 Todo 반환
        } else {
            // Todo가 존재하지 않으면 로그 출력
            System.out.println("Todo ID: " + listId + " - 해당 Todo를 찾을 수 없습니다.");
            return null;  // Todo가 없으면 null 반환
        }
    }



    // Todo 삭제
    public void deleteTodo(Integer listId) {
        System.out.println("Todo 삭제 요청: ID = " + listId);  // 삭제 요청 로그

        if (todoRepository.existsById(listId)) {
            todoRepository.deleteById(listId);
            System.out.println("Todo 삭제 성공: ID = " + listId);  // 삭제 성공 로그
        } else {
            System.out.println("ID 찾기 실패입니다. Todo가 존재하지 않습니다. ID = " + listId);  // Todo가 없을 때 실패 로그
        }
    }

    // 새로운 Todo 항목 생성
    public Todo createTodo(Todo newTodo) {
        // DB에 새 Todo 항목 저장하고 반환
        return todoRepository.save(newTodo);  // Repository에서 새 Todo 항목을 저장하고 반환
    }

}
