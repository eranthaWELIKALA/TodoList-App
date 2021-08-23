package com.test.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {
    private List<Todo> todoList = new ArrayList<>();
    private final TodoSwipeCallbackListener todoSwipeCallbackListener;
    private final CompletedCheckboxListener completedCheckboxListener;

    public TodoAdapter(TodoSwipeCallbackListener todoSwipeCallbackListener, CompletedCheckboxListener completedCheckboxListener) {
        this.todoSwipeCallbackListener = todoSwipeCallbackListener;
        this.completedCheckboxListener = completedCheckboxListener;
    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_view_item, parent, false);
        return new TodoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoHolder holder, int position) {
        final Todo currentTodo = todoList.get(position);
        holder.name.setText(currentTodo.getName());
        holder.description.setText(currentTodo.getDescription());
        holder.date.setText(CustomDate.getStringDate(currentTodo.getDate()));
        if (currentTodo.getCompletedDate() != null) {
            holder.completedDate.setText(CustomDate.getStringDate(currentTodo.getCompletedDate()));
            holder.group2.setVisibility(View.VISIBLE);
        }
        else {
            holder.completedDate.setText("");
            holder.group2.setVisibility(View.GONE);
        }
        holder.completed.setChecked(currentTodo.isCompleted());
        holder.completed.setOnClickListener(v -> handleClick(holder, currentTodo));
    }


    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setTodoList(List<Todo> todoList) {
        if (todoList != null) {
            this.todoList = todoList;
        }
        else {
            this.todoList.clear();
        }
        notifyDataSetChanged();
    }

    private void handleClick(TodoHolder holder, Todo currentTodo) {
        boolean checked = holder.completed.isChecked();
        if (checked) {
            currentTodo.setCompletedDate(new Date());
        }
        else {
            currentTodo.setCompletedDate(null);
        }
        currentTodo.setCompleted(checked);
        completedCheckboxListener.updateTodo(currentTodo);
    }


    class TodoHolder extends RecyclerView.ViewHolder {

        private RelativeLayout group2;
        private TextView name;
        private TextView description;
        private CheckBox completed;
        private TextView date, createdDate, completedDate;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.todo_view_item_todo_name);
            description = itemView.findViewById(R.id.todo_view_item_description);
            date = itemView.findViewById(R.id.todo_view_item_date);
            completedDate = itemView.findViewById(R.id.todo_view_item_completed_date);
            completed = itemView.findViewById(R.id.todo_view_item_completed);
            group2 = itemView.findViewById(R.id.group_2);
        }
    }

    public void showDeleteTodoDialog(int position) {
        if (todoSwipeCallbackListener != null) {
            if (todoList != null ) {
                Todo selectedTodo = todoList.get(position);
                todoSwipeCallbackListener.deleteCallback(selectedTodo);
            }
        }
        this.notifyItemChanged(position);
    }

    public void showEditTodoDialog(int position) {
        if (todoSwipeCallbackListener != null) {
            if (todoList != null ) {
                Todo selectedTodo = todoList.get(position);
                todoSwipeCallbackListener.updateCallback(selectedTodo);
            }
        }
        this.notifyItemChanged(position);
    }
}
