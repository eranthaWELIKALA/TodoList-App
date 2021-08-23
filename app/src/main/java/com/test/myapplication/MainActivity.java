package com.test.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements TodoSwipeCallbackListener, CompletedCheckboxListener {

    private int height;

    private TodoViewModel todoViewModel;
    private ScrollView data;
    private TextView message, selectedView;
    private RecyclerView todoRecyclerView;
    private Button remainingBtn, completedBtn;

    private FloatingActionButton addTodoBtn;

    private TodoAdapter todoAdapter;

    private List<Todo> remainingList, completedList;

    private boolean remaining_global = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        height = Resources.getSystem().getDisplayMetrics().heightPixels;

        data = findViewById(R.id.view_todo_list);
        message = findViewById(R.id.view_message);
        selectedView = findViewById(R.id.selected_view);
        todoRecyclerView = findViewById(R.id.view_todo_list_rv);

        addTodoBtn =  findViewById(R.id.view_add_edit_todo_btn);
        addTodoBtn.setOnClickListener(this::onClick);

        remainingBtn =  findViewById(R.id.remaining_btn);
        remainingBtn.setOnClickListener(v -> triggerView(v, true));
        completedBtn =  findViewById(R.id.completed_btn);
        completedBtn.setOnClickListener(v -> triggerView(v, false));

        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setHasFixedSize(false);

        todoAdapter = new TodoAdapter(this, this);
        todoRecyclerView.setAdapter(todoAdapter);

        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    todoAdapter.showDeleteTodoDialog(viewHolder.getAdapterPosition());
                }
                else if (direction == ItemTouchHelper.RIGHT) {
                    todoAdapter.showEditTodoDialog(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onChildDraw(Canvas canvas, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (dX > 0) {
                    new RecyclerViewSwipeDecorator.Builder(getApplicationContext(), canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addActionIcon(R.drawable.ic_add_edit_24)
                            .create()
                            .decorate();
                } else if (dX < 0) {
                    new RecyclerViewSwipeDecorator.Builder(getApplicationContext(), canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addActionIcon(R.drawable.ic_delete_24)
                            .create()
                            .decorate();
                } else {
                    new RecyclerViewSwipeDecorator.Builder(getApplicationContext(), canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addActionIcon(R.drawable.ic_add_edit_24)
                            .create()
                            .decorate();
                }

                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(todoRecyclerView);

        todoViewModel = new ViewModelProvider(this, new TodoViewModelFactory(getApplication()))
                .get(TodoViewModel.class);
        todoViewModel.getTodoList().observe(this, todoList -> {
            if (todoList != null) {
                data.setVisibility(View.VISIBLE);
                message.setVisibility(View.GONE);
                remainingList = new ArrayList<>();
                completedList = new ArrayList<>();
                for(Todo todo: todoList) {
                    if (todo.isCompleted()) {
                        completedList.add(todo);
                    }
                    else {
                        remainingList.add(todo);
                    }
                }
                if (remaining_global) {
                    todoAdapter.setTodoList(remainingList);
                }
                else {
                    todoAdapter.setTodoList(completedList);
                }

            }
            else {
                todoAdapter.setTodoList(null);
                data.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
            }
        });
        selectedView.setText(R.string.remaining_list);
        remainingBtn.setEnabled(false);
        completedBtn.setEnabled(true);
    }

    private void triggerView(View v, boolean remaining) {
        remaining_global = remaining;
        if (todoAdapter != null) {
            if(remaining) {
                selectedView.setText(R.string.remaining_list);
                remainingBtn.setEnabled(false);
                completedBtn.setEnabled(true);
                todoAdapter.setTodoList(remainingList);
            }
            else {
                selectedView.setText(R.string.completed_list);
                remainingBtn.setEnabled(true);
                completedBtn.setEnabled(false);
                todoAdapter.setTodoList(completedList);
            }
        }
    }

    private void onClick(View v) {
        showAddTodoDialog();
    }

    private void showAddTodoDialog() {
        Todo todo = new Todo();
        AddEditTodoDialog addEdiTodoDialog = new AddEditTodoDialog(this, height, addEditTodoDialogListener, false, todo);
        addEdiTodoDialog.setCanceledOnTouchOutside();
        addEdiTodoDialog.showBottomSheetDialog();
    }

    @Override
    public void deleteCallback(Todo todo) {
        showDeleteTodoConfirmDialog(todo);
    }

    @Override
    public void updateCallback(Todo todo) {
        showEditTodoDialog(todo);
    }

    private void showEditTodoDialog(Todo todo) {
        AddEditTodoDialog addEditTodoDialog = new AddEditTodoDialog(this, height, addEditTodoDialogListener, true, todo);
        addEditTodoDialog.setCanceledOnTouchOutside();
        addEditTodoDialog.showBottomSheetDialog();
    }

    private final AddEditTodoDialogListener addEditTodoDialogListener = new AddEditTodoDialogListener() {
        @Override
        public void onConfirm(Todo todo, boolean edit) {
            if (edit) {
                Handler updateTodoHandler = new Handler(Looper.getMainLooper(), updateTodoMessage -> {
                    boolean updateTodoResult = (boolean) updateTodoMessage.obj;
                    if (updateTodoResult) {
                        Toast.makeText(getApplicationContext(), "Successfully updated the todo", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to update the todo", Toast.LENGTH_LONG).show();
                    }
                    return false;
                });
                todoViewModel.updateTodo(todo, updateTodoHandler);
            } else {
                Handler insertTodoHandler = new Handler(Looper.getMainLooper(), message -> {
                    String result = (String) message.obj;
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    return false;
                });
                todoViewModel.insertTodo(todo, insertTodoHandler);
            }
        }

        @Override
        public void onCancel() {
            // nothing to do
        }
    };

    private void showDeleteTodoConfirmDialog(Todo todo) {
        ConfirmDialogListener deleteTodoConfirmDialogListener = new ConfirmDialogListener() {
            @Override
            public void onConfirm() {
                Handler deleteTodoHandler = new Handler(Looper.getMainLooper(), deleteTodoMessage -> {
                    boolean deleteTodoResult = (boolean) deleteTodoMessage.obj;
                    if (deleteTodoResult) {
                        Toast.makeText(getApplicationContext(), "Successfully deleted the todo", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to deleted the todo", Toast.LENGTH_LONG).show();
                    }
                    return false;
                });
                todoViewModel.deleteTodo(todo, deleteTodoHandler);
            }

            @Override
            public void onCancel() {

            }
        };

        ConfirmDialog deleteEntryConfirmDialog = new ConfirmDialog(this, height, deleteTodoConfirmDialogListener, true, true);
        deleteEntryConfirmDialog.setMessage(
                "Do you really want to delete this todo? \n" +
                        "Title: " + todo.getName() + " - " + todo.getDescription() + "\n" +
                        "Date: " + CustomDate.getStringDate(todo.getDate())
        );
        deleteEntryConfirmDialog.setPositiveBtnText("Delete");
        deleteEntryConfirmDialog.setNegativeBtnText("Cancel");
        deleteEntryConfirmDialog.setCanceledOnTouchOutside();
        deleteEntryConfirmDialog.showBottomSheetDialog();
    }

    @Override
    public void updateTodo(Todo todo) {
        Handler updateTodoHandler = new Handler(Looper.getMainLooper(), updateTodoMessage -> {
            boolean updateTodoResult = (boolean) updateTodoMessage.obj;
            if (updateTodoResult) {
                Toast.makeText(getApplicationContext(), "Successfully updated the todo", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to update the todo", Toast.LENGTH_LONG).show();
            }
            return false;
        });
        todoViewModel.updateTodo(todo, updateTodoHandler);
    }
}