package com.qr.core.popup.impl;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.qr.core.R;
import com.qr.core.popup.animator.PopupAnimator;
import com.qr.core.popup.base.CenterPopupView;


public class ConfirmPopupView extends CenterPopupView implements View.OnClickListener {
    private String title;
    private String content;
    private OnCancelListener cancelListener;
    private OnConfirmListener confirmListener;
    private boolean isConfirm = false;

    public ConfirmPopupView(@NonNull Context context,String title,String content,OnCancelListener cancelListener,OnConfirmListener confirmListener) {
        super(context);
        this.title = title;
        this.content = content;
        this.cancelListener = cancelListener;
        this.confirmListener = confirmListener;
    }

    @Override
    protected void onCreate() {
        AppCompatTextView titleTv = findViewById(R.id._confirm_popup_view_title_tv);
        AppCompatTextView contentTv = findViewById(R.id._confirm_popup_view_content_tv);
        AppCompatTextView cancelTv = findViewById(R.id._confirm_popup_view_cancel_tv);
        AppCompatTextView okTv = findViewById(R.id._confirm_popup_view_ok_tv);

        titleTv.setText(title);
        contentTv.setText(content);
        cancelTv.setOnClickListener(this);
        okTv.setOnClickListener(this);
    }

    @Override
    protected void onShow() {
    }

    @Override
    protected void onDismiss() {
        if(isConfirm){
            confirmListener.onConfirm();
        }
        else{
            cancelListener.onCancel();
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

    public static class Builder{
        Context context;
        private String title;
        private String content;
        private OnCancelListener cancelListener;
        private OnConfirmListener confirmListener;
        private PopupAnimator animator;
        private boolean isShadowBackground = true;
        private boolean isDismissOnTouchOutside = true;
        private boolean isRequestFocus = true;
        private boolean isDismissOnBackPressed = true;
        private boolean isAutoOpenSoftInput = false;

        public Builder(@NonNull Context context){
            this.context = context;
        }
        public Builder title(String title){
            this.title = title;
            return this;
        }
        public Builder content(String content){
            this.content = content;
            return this;
        }
        public Builder cancel(OnCancelListener listener){
            this.cancelListener = listener;
            return this;
        }
        public Builder confirm(OnConfirmListener listener){
            this.confirmListener = listener;
            return this;
        }
        public Builder setAnimator(PopupAnimator animator) {
            this.animator = animator;
            return this;
        }
        public Builder setShadowBackground(boolean shadowBackground) {
            this.isShadowBackground = shadowBackground;
            return this;
        }
        public void setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
            isDismissOnTouchOutside = dismissOnTouchOutside;
        }
        public void setRequestFocus(boolean requestFocus) {
            isRequestFocus = requestFocus;
        }
        public void setDismissOnBackPressed(boolean dismissOnBackPressed) {
            isDismissOnBackPressed = dismissOnBackPressed;
        }
        public void setAutoOpenSoftInput(boolean autoOpenSoftInput) {
            isAutoOpenSoftInput = autoOpenSoftInput;
        }

        public ConfirmPopupView build(){
            ConfirmPopupView confirmPopupView = new ConfirmPopupView(context, title, content, cancelListener, confirmListener);
            confirmPopupView.setPopupAnimator(animator);
            confirmPopupView.setShadowBackground(isShadowBackground);
            confirmPopupView.setAutoOpenSoftInput(isAutoOpenSoftInput);
            confirmPopupView.setDismissOnBackPressed(isDismissOnBackPressed);
            confirmPopupView.setDismissOnTouchOutside(isDismissOnTouchOutside);
            confirmPopupView.setRequestFocus(isRequestFocus);
            return confirmPopupView;
        }

    }

    public interface OnCancelListener {
        void onCancel();
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}
