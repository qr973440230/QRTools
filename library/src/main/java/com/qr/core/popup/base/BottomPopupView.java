package com.qr.core.popup.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.qr.core.R;
import com.qr.core.popup.animator.PopupAnimator;
import com.qr.core.popup.animator.TranslateAnimator;

import static com.qr.core.popup.animator.PopupAnimation.TranslateFromBottom;

public abstract class BottomPopupView extends BasePopupView{
    protected FrameLayout bottomPopupView;
    public BottomPopupView(@NonNull Context context) {
        super(context);

        bottomPopupView = findViewById(R.id._bottom_popup_container);
        View view = LayoutInflater.from(getContext()).inflate(getImplLayoutId(),bottomPopupView,false);
        bottomPopupView.addView(view);
    }

    @NonNull
    @Override
    protected PopupAnimator getDefaultPopupAnimator() {
        return new TranslateAnimator(getPopupContentView(), TranslateFromBottom);
    }

    @Override
    protected int getPopupViewLayoutId() {
        return R.layout._bottom_popup_view;
    }
}
