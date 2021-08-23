package com.test.myapplication;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private final TodoRepository todoRepository;

    private LiveData<List<Todo>> todoList;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        todoRepository = TodoRepository.getInstance(application);

        todoList = todoRepository.getTodoList();
    }

    public void insertTodo(Todo todo, Handler handler) {
        todoRepository.insertTodo(todo, handler);
    }

    public void deleteTodo(Todo todo, Handler handler) {
        todoRepository.deleteTodo(todo, handler);
    }

    public void updateTodo(Todo todo, Handler handler) {
        todoRepository.updateTodo(todo, handler);
    }

    public LiveData<List<Todo>> getTodoList() {
        return todoList;
    }
}
