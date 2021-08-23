package com.test.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ConfirmDialog {
    private final BottomSheetDialog bottomSheetDialog;
    private final Context context;
    private final LinearLayout messageArea;
    private final TextView message;
    private final TextView negative_btn, positive_btn;

    public ConfirmDialog(Context context, int height, ConfirmDialogListener confirmDialogListener, boolean positiveBtn, boolean negativeBtn){
        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.context = context;
        this.bottomSheetDialog.setContentView(R.layout.confirm_dialog);
        this.negative_btn = bottomSheetDialog.findViewById(R.id.confirm_dialog_negative_btn);
        this.positive_btn = bottomSheetDialog.findViewById(R.id.confirm_dialog_positive_btn);
        this.messageArea = bottomSheetDialog.findViewById(R.id.confirm_dialog_message_area);
        this.message = bottomSheetDialog.findViewById(R.id.confirm_dialog_message);


        int width = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int newHeight = (int)(context.getResources().getDisplayMetrics().heightPixels * 0.90);
        bottomSheetDialog.getWindow().setLayout(width, newHeight);

        bottomSheetDialog.getBehavior().setPeekHeight(height);
        bottomSheetDialog.setCancelable(false);

        if (negativeBtn && this.negative_btn != null) {
            this.negative_btn.setVisibility(View.VISIBLE);
            this.negative_btn.setOnClickListener(view -> {
                confirmDialogListener.onCancel();
                closeDialog();
            });
        }

        if (positiveBtn && this.positive_btn != null) {
            this.positive_btn.setVisibility(View.VISIBLE);
            this.positive_btn.setOnClickListener(view -> {
                confirmDialogListener.onConfirm();
                closeDialog();
            });
        }
    }

    public void setMessage(String message) {
        if (message != null && !message.equals("")) {
            this.messageArea.setVisibility(View.VISIBLE);
            this.message.setText(message);
        }
    }

    public void setPositiveBtnText(String title) {
        if (title != null && !title.equals("")) {
            this.positive_btn.setText(title);
        }
    }

    public void setNegativeBtnText(String title) {
        if (title != null && !title.equals("")) {
            this.negative_btn.setText(title);
        }
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
