package com.test.myapplication;

public interface AddEditTodoDialogListener {
    void onConfirm(Todo todo, boolean edit);
    void onCancel();
}
