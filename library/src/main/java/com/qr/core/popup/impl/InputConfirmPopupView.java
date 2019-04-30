package com.qr.core.popup.impl;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.qr.core.R;
import com.qr.core.popup.base.CenterPopupView;
import com.qr.core.popup.base.PopupViewConfig;


public class InputConfirmPopupView extends CenterPopupView{
    private String title;
    private String hint;
    private OnCancelListener cancelListener;
    private OnConfirmListener confirmListener;
    private boolean isConfirm = false;
    private AppCompatEditText contentEd;

    public InputConfirmPopupView(@NonNull Context context, PopupViewConfig popupViewConfig) {
        super(context,popupViewConfig);
    }

    @Override
    protected void onCreate() {
        AppCompatTextView titleTv = findViewById(R.id.__input_confirm_popup_view_title_tv);
        contentEd = findViewById(R.id._input_confirm_popup_view_content_ed);
        AppCompatTextView cancelTv = findViewById(R.id._input_confirm_popup_view_cancel_tv);
        AppCompatTextView okTv = findViewById(R.id._input_confirm_popup_view_ok_tv);

        if(title != null){
            titleTv.setText(title);
        }
        if(hint != null){
            contentEd.setHint(hint);
        }
        cancelTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isConfirm = true;
                dismiss();
            }
        });
    }

    @Override
    protected void onShow() {
    }

    @Override
    protected void onDismiss() {
        if(isConfirm){
            if(confirmListener != null){
                confirmListener.onConfirm(contentEd.getText());
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
        return R.layout._input_confirm_popup_view;
    }

    public InputConfirmPopupView setTitle(String title) {
        this.title = title;
        return this;
    }

    public InputConfirmPopupView setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public InputConfirmPopupView setCancelListener(OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }

    public InputConfirmPopupView setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
        return this;
    }

    public interface OnCancelListener {
        void onCancel();
    }

    public interface OnConfirmListener {
        void onConfirm(CharSequence string);
    }
}
