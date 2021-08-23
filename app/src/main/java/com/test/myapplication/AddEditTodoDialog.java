package com.test.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

import static com.test.myapplication.CustomDate.GetDate;

public class AddEditTodoDialog {
    private final BottomSheetDialog bottomSheetDialog;
    private final TextView negative_btn, positive_btn, header;
    private EditText name, description, date;

    public AddEditTodoDialog(Context context, int height, AddEditTodoDialogListener addEditTodoDialogListener, boolean edit, Todo todo){
        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.add_edit_todo_dialog);
        this.header = bottomSheetDialog.findViewById(R.id.add_edit_todo_dialog_header);
        this.negative_btn = bottomSheetDialog.findViewById(R.id.add_edit_todo_dialog_negative_btn);
        this.positive_btn = bottomSheetDialog.findViewById(R.id.add_edit_todo_dialog_positive_btn);
        this.name = bottomSheetDialog.findViewById(R.id.add_edit_todo_dialog_todo_name);
        this.description = bottomSheetDialog.findViewById(R.id.add_edit_todo_dialog_todo_description);
        this.date = bottomSheetDialog.findViewById(R.id.add_edit_todo_dialog_todo_date);

        int width = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int newHeight = (int)(context.getResources().getDisplayMetrics().heightPixels * 0.90);
        bottomSheetDialog.getWindow().setLayout(width, newHeight);

        bottomSheetDialog.getBehavior().setPeekHeight(height);
        bottomSheetDialog.setCancelable(false);

        if (todo != null) {
            if (edit) {
                if (todo.getName() != null) this.name.setText(todo.getName());
                if (todo.getDescription() != null) this.description.setText(todo.getDescription());
                if (todo.getDate() != null) {
                    try {
                        new DatePickerHandler(context, date, todo.getDate());
                    } catch (Exception ignored) {
                    }
                }
            }
            else {
                try {
                    new DatePickerHandler(context, date, null);
                } catch (Exception ignored) {
                }
            }
        }

        if (edit && header != null && positive_btn != null) {
                this.header.setText(R.string.add_edit_todo_dialog_header_edit);
        }

        if (this.negative_btn != null) {
            this.negative_btn.setVisibility(View.VISIBLE);
            this.negative_btn.setOnClickListener(view -> {
                addEditTodoDialogListener.onCancel();
                closeDialog();
            });
        }

        if (this.positive_btn != null) {
            this.positive_btn.setVisibility(View.VISIBLE);
            if (edit) {
                this.positive_btn.setText(R.string.add_edit_todo_dialog_positive_btn__edit_tag);
            }
            this.positive_btn.setOnClickListener(view -> {
                String todoName = name.getText().toString();
                String todoDescription = description.getText().toString();
                String todoDate = date.getText().toString();
                if (todo != null) {
                    todo.setName(todoName);
                    todo.setDescription(todoDescription);
                    todo.setDate(GetDate(todoDate));
                    ValidationResult validationResult = todoValidate(todo);
                    if (validationResult.result) {
                        addEditTodoDialogListener.onConfirm(todo, edit);
                        closeDialog();
                    }
                    else {
                        Toast.makeText(context, validationResult.message, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show();
                    closeDialog();
                }
            });
        }
    }

    private ValidationResult todoValidate(Todo todo) {
        String name = todo.getName();
        String description = todo.getDescription();
        Date date = todo.getDate();

        if (name == null || name.equalsIgnoreCase("")) {
            return new ValidationResult(false, "Todo title is required");
        }

        if (description == null || description.equalsIgnoreCase("")) {
            return new ValidationResult(false, "Todo description is required");
        }
        if (date == null ) {
            return new ValidationResult(false, "Todo date is required");
        }
        return new ValidationResult(true, "Success");
    }

    public void showBottomSheetDialog(){
        bottomSheetDialog.show();
    }

    public void closeDialog(){
        bottomSheetDialog.dismiss();
    }

    public void setCanceledOnTouchOutside(){
        bottomSheetDialog.setCanceledOnTouchOutside(true);
    }

    public interface BottomSheetDialogListener {
        void getSelectedOptionId(int option);
    }
}
