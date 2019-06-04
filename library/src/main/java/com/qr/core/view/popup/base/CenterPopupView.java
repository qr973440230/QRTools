package com.qr.core.view.popup.base;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qr.core.R;
import com.qr.core.view.popup.animator.PopupAnimator;
import com.qr.core.view.popup.animator.ScaleAlphaAnimator;

import static com.qr.core.view.popup.enums.PopupAnimation.ScaleAlphaFromCenter;

public abstract class CenterPopupView extends BasePopup{
    public CenterPopupView(@NonNull Context context) {
        super(context);
    }

    public CenterPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected final int getPopupLayoutId(){
        return R.layout._xpopup_center_popup_view;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new ScaleAlphaAnimator(layoutView, ScaleAlphaFromCenter);
    }
}
