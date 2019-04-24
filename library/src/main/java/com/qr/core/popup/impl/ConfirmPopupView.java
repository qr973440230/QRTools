package com.qr.core.popup.impl;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.qr.core.R;
import com.qr.core.popup.base.CenterPopupView;
import com.qr.core.popup.interfaces.OnCancelListener;
import com.qr.core.popup.interfaces.OnConfirmListener;

public class ConfirmPopupView extends CenterPopupView implements View.OnClickListener {
    private String title;
    private String content;
    private OnCancelListener cancelListener;
    private OnConfirmListener confirmListener;

    public ConfirmPopupView(@NonNull Context context,String title,String content,OnCancelListener cancelListener,OnConfirmListener confirmListener) {
        super(context);
        this.title = title;
        this.content = content;
        this.cancelListener = cancelListener;
        this.confirmListener = confirmListener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        AppCompatTextView titleTv;
        titleTv = findViewById(R.id._confirm_popup_view_title_tv);
        AppCompatTextView contentTv;
        contentTv = findViewById(R.id._confirm_popup_view_content_tv);
        AppCompatTextView cancelTv;
        cancelTv = findViewById(R.id._confirm_popup_view_cancel_tv);
        AppCompatTextView okTv = findViewById(R.id._confirm_popup_view_ok_tv);

        titleTv.setText(title);
        contentTv.setText(content);
        cancelTv.setOnClickListener(this);
        okTv.setOnClickListener(this);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._confirm_popup_view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id._confirm_popup_view_ok_tv){
            confirmListener.onConfirm();
        }else if(v.getId() == R.id._confirm_popup_view_cancel_tv){
            cancelListener.onCancel();
        }

        dismiss();
    }

    public static class Builder{
        Context context;
        private String title;
        private String content;
        private OnCancelListener cancelListener;
        private OnConfirmListener confirmListener;

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
        public ConfirmPopupView build(){
            return new ConfirmPopupView(context,title,content,cancelListener,confirmListener);
        }
    }
}
