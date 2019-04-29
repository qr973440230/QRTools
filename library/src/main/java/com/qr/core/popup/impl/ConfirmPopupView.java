package com.qr.core.popup.impl;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.qr.core.R;
import com.qr.core.popup.base.CenterPopupView;


public class ConfirmPopupView extends CenterPopupView implements View.OnClickListener {
    private String title;
    private String content;
    private OnCancelListener cancelListener;
    private OnConfirmListener confirmListener;
    private boolean isConfirm = false;

    public ConfirmPopupView(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate() {
        AppCompatTextView titleTv = findViewById(R.id._confirm_popup_view_title_tv);
        AppCompatTextView contentTv = findViewById(R.id._confirm_popup_view_content_tv);
        AppCompatTextView cancelTv = findViewById(R.id._confirm_popup_view_cancel_tv);
        AppCompatTextView okTv = findViewById(R.id._confirm_popup_view_ok_tv);

        if(title != null){
            titleTv.setText(title);
        }
        if(content != null){
            contentTv.setText(content);
        }

        cancelTv.setOnClickListener(this);
        okTv.setOnClickListener(this);
    }

    @Override
    protected void onShow() {
    }

    @Override
    protected void onDismiss() {
        if(isConfirm){
            if(confirmListener != null){
                confirmListener.onConfirm();
            }
        }
        else{
            if(cancelListener != null){
                cancelListener.onCancel();
            }
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._confirm_popup_view_impl;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id._confirm_popup_view_ok_tv){
            isConfirm = true;
        }
        dismiss();
    }

    public ConfirmPopupView setTitle(String title) {
        this.title = title;
        return this;
    }

    public ConfirmPopupView setContent(String content) {
        this.content = content;
        return this;
    }

    public ConfirmPopupView setCancelListener(OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }

    public ConfirmPopupView setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
        return this;
    }

    public ConfirmPopupView setConfirm(boolean confirm) {
        isConfirm = confirm;
        return this;
    }

    public interface OnCancelListener {
        void onCancel();
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}
