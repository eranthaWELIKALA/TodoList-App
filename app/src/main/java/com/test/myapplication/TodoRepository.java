package com.test.myapplication;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class TodoRepository {
    private static volatile TodoRepository instance;

    private final TodoDAO todoDAO;
    private final Handler handler;
    private Looper mainLooper;

    private LiveData<List<Todo>> todoList;

    private TodoRepository(Application application) {
        TodoDatabase moneyManagerDB = TodoDatabase.getAppDatabase(application);
        todoDAO = moneyManagerDB.TodoDAO();
        mainLooper = application.getMainLooper();
        handler = new Handler(application.getMainLooper());
    }

    public static TodoRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TodoRepository(application);
        }
        return instance;
    }

    public LiveData<List<Todo>> getTodoList() {
        if (todoList == null) {
            todoList = todoDAO.getTodoList();
        }
        return todoList;
    }

    public void insertTodo(Todo todo, Handler externalHandler) {
        AsyncTaskExecutor<String> insert = new InsertTodoTask(todoDAO, todo);
        insert.setExternalHandler(externalHandler).execute();
    }

    public void deleteTodo(Todo todo, Handler externalHandler) {
        AsyncTaskExecutor<Boolean> delete = new TodoRepository.DeleteTodoTask(todoDAO, todo);
        delete.setExternalHandler(externalHandler).execute();
    }

    public void updateTodo(Todo todo, Handler externalHandler) {
        AsyncTaskExecutor<Boolean> update = new UpdateTodoTask(todoDAO, todo);
        update.setExternalHandler(externalHandler).execute();
    }

    public static class InsertTodoTask extends AsyncTaskExecutor<String> {

        private final TodoDAO todoDAO;
        private final Todo todo;

        public InsertTodoTask(TodoDAO dao, Todo p) {
            todoDAO = dao;
            todo = p;
        }
        @Override
        public void onPreExecute() {

        }

        @Override
        public String doInBackground() {
            long id;
            try {
                todo.setCreatedDate(new Date());
                id = todoDAO.insert(todo);
                if (id < 0) {
                    throw new Exception("Inserting transaction is failed");
                }
                else {
                    return "SUCCESS";
                }
            }
            catch (Exception ignored) {
                return "FAILED";
            }
        }

        @Override
        public void onPostExecute(String l) {
        }
    }



    public static class DeleteTodoTask extends AsyncTaskExecutor<Boolean> {

        private final TodoDAO todoDAO;
        private final Todo todo;

        public DeleteTodoTask(TodoDAO dao, Todo p) {
            todoDAO = dao;
            todo = p;
        }
        @Override
        public void onPreExecute() {

        }

        @Override
        public Boolean doInBackground() {
            boolean success = false;
            try {
                todoDAO.delete(todo);
                success = true;
            }
            catch (Exception ignored) {
            }
            return success;
        }

        @Override
        public void onPostExecute(Boolean l) {
        }
    }

    public static class UpdateTodoTask extends AsyncTaskExecutor<Boolean> {

        private final TodoDAO todoDAO;
        private final Todo todo;

        public UpdateTodoTask(TodoDAO dao, Todo p) {
            todoDAO = dao;
            todo = p;
        }
        @Override
        public void onPreExecute() {

        }

        @Override
        public Boolean doInBackground() {
            boolean success = false;
            try {
                todoDAO.update(todo);
                success = true;
            }
            catch (Exception ignored) {
            }
            return success;
        }

        @Override
        public void onPostExecute(Boolean l) {
        }
    }
}
