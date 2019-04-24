package com.qr.core.popup.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.qr.core.R;


public abstract class CenterPopupView extends BasePopupView {
    protected FrameLayout centerPopupView;
    public CenterPopupView(@NonNull Context context) {
        super(context);

        centerPopupView = findViewById(R.id._center_popup_container);
        View view = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), centerPopupView, false);
        centerPopupView.addView(view);
    }

    @Override
    protected int getPopupViewLayoutId() {
        return R.layout._center_popup_view;
    }
    @LayoutRes
    protected abstract int getImplLayoutId();
}
