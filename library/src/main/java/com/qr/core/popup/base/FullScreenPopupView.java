package com.qr.core.popup.base;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.qr.core.R;
import com.qr.core.popup.animator.PopupAnimator;
import com.qr.core.popup.animator.ScaleAlphaAnimator;
import com.qr.core.utils.WindowUtils;

import static com.qr.core.popup.animator.PopupAnimation.ScaleAlphaFromCenter;

/**
 * Created by QR on 2019/4/29 10:24
 */
public abstract class FullScreenPopupView extends BasePopupView{

    public FullScreenPopupView(@NonNull Context context,PopupViewConfig popupViewConfig) {
        super(context,popupViewConfig);

        FrameLayout fullscreenPopupView = findViewById(R.id._fullscreen_popup_container);
        View view = LayoutInflater.from(context).inflate(getImplLayoutId(), fullscreenPopupView, false);
        ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
        if(WindowUtils.isStatusBarVisible((Activity) getContext())){
            layoutParams.topMargin = WindowUtils.getStatusBarHeight(context);
        }
        if(WindowUtils.isNavigationBarVisible((Activity) getContext())){
            layoutParams.bottomMargin = WindowUtils.getNavigationBarHeight(context);
        }
        view.setLayoutParams(layoutParams);
        fullscreenPopupView.addView(view);
    }

    @NonNull
    @Override
    protected PopupAnimator getDefaultPopupAnimator() {
        return new ScaleAlphaAnimator(getPopupContentView(), ScaleAlphaFromCenter);
    }

    @Override
    protected final int getPopupViewLayoutId() {
        return R.layout._fullscreen_popup_view;
    }
}
