package com.qr.core.popup.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.qr.core.R;
import com.qr.core.popup.animator.PopupAnimator;
import com.qr.core.popup.animator.ShadowBgAnimator;
import com.qr.core.utils.KeyboardUtils;
import com.qr.core.popup.util.PopupUtils;

import java.util.ArrayList;

public abstract class BasePopupView extends FrameLayout {
    private PopupAnimator popupAnimator;
    private ShadowBgAnimator shadowBgAnimator;
    private boolean isShadowBackground = true;
    private boolean isDismissOnTouchOutside = true;
    private boolean isRequestFocus = true;
    private boolean isDismissOnBackPressed = true;
    private boolean isAutoOpenSoftInput = false;

    public BasePopupView(@NonNull Context context) {
        super(context);

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        shadowBgAnimator = new ShadowBgAnimator(BasePopupView.this);
        shadowBgAnimator.initAnimator();

        View view = LayoutInflater.from(context).inflate(getPopupViewLayoutId(), this, false);
        view.setAlpha(0f);
        addView(view);
    }

    public void show(){
        final Activity activity = (Activity)getContext();
        final ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();

        KeyboardUtils.registerSoftInputChangedListener(activity, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if(height == 0){
                    PopupUtils.moveDown(BasePopupView.this);
                }else{
                    PopupUtils.moveUpToKeyboard(height,BasePopupView.this);
                }
            }
        });

        // DecorView完成布局 将PopupView添加到DecorView
        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                viewGroup.addView(BasePopupView.this,new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                create();
            }
        });
    }

    public void dismiss(){
        clearFocus();
        dismissAnimation();
        doAfterDismiss();
    }

    private void create(){
        onCreate();
        // TODO: onCreate

        // 等布局完成时才进行显示
        post(new Runnable() {
            @Override
            public void run() {
                getPopupContentView().setAlpha(1.0f);
                if(popupAnimator == null){
                    popupAnimator = getDefaultPopupAnimator();
                    popupAnimator.initAnimator();
                }

                showAnimation();
                doAfterShow();
            }
        });
    }

    private void showAnimation(){
        if(isShadowBackground)
            shadowBgAnimator.animateShow();
        popupAnimator.animateShow();
    }
    private void dismissAnimation(){
        if(isShadowBackground)
            shadowBgAnimator.animateDismiss();
        popupAnimator.animateDismiss();
    }
    private void doAfterShow(){
        removeCallbacks(doAfterShowTask);
        postDelayed(doAfterShowTask,popupAnimator.getDuration());
    }
    private Runnable doAfterShowTask = new Runnable() {
        @Override
        public void run() {
            onShow();
            // TODO: onShow

            // 处理返回键和焦点
            focusAndProcessBackPress();
        }
    };

    private void focusAndProcessBackPress(){
        // 是否抢占焦点
        if(isRequestFocus){
            setFocusableInTouchMode(true);
            requestFocus();
        }

        // 此处焦点可能被内容的EditText抢走，也需要给EditText也设置返回按下监听
        setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (isDismissOnBackPressed)
                        dismiss();
                    return true;
                }
                return false;
            }
        });

        ArrayList<EditText> list = new ArrayList<>();
        PopupUtils.findAllEditText(list, (ViewGroup) getPopupContentView());
        for (int i = 0; i < list.size(); i++) {
            final View et = list.get(i);
            if (i == 0) {
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocus();
                if (isAutoOpenSoftInput) {
                    if(showSoftInputTask==null){
                        showSoftInputTask = new ShowSoftInputTask(et);
                    }else {
                        removeCallbacks(showSoftInputTask);
                    }
                    postDelayed(showSoftInputTask, 10);
                }
            }
            et.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (isDismissOnBackPressed)
                            dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
    private ShowSoftInputTask showSoftInputTask;
    class ShowSoftInputTask implements Runnable{
        View focusView;
        boolean isDone = false;
        ShowSoftInputTask(View focusView){
            this.focusView = focusView;
        }
        @Override
        public void run() {
            if(focusView!=null && !isDone){
                isDone = true;
                KeyboardUtils.showSoftInput(focusView);
            }
        }
    }

    private void doAfterDismiss(){
        KeyboardUtils.hideSoftInput(this);
        removeCallbacks(doAfterDismissTask);
        postDelayed(doAfterDismissTask,popupAnimator.getDuration());
    }
    private Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            onDismiss();
            // TODO: onDismiss
            if(isRequestFocus){
                View contentView = ((Activity)getContext()).findViewById(android.R.id.content);
                contentView.setFocusable(true);
                contentView.setFocusableInTouchMode(true);
            }

            // 移除自己
            final Activity activity = (Activity)getContext();
            final ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
            viewGroup.removeView(BasePopupView.this);
            KeyboardUtils.removeLayoutChangeListener(viewGroup);
        }
    };

    public View getPopupContentView(){
        return getChildAt(0);
    }
    public View getPopupImplView() {
        return ((ViewGroup) getPopupContentView()).getChildAt(0);
    }

    public void setPopupAnimator(PopupAnimator popupAnimator) {
        this.popupAnimator = popupAnimator;
    }
    public void setShadowBackground(boolean shadowBackground) {
        this.isShadowBackground = shadowBackground;
    }
    public void setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.isDismissOnTouchOutside = dismissOnTouchOutside;
    }
    public void setRequestFocus(boolean requestFocus) {
        this.isRequestFocus = requestFocus;
    }
    public void setDismissOnBackPressed(boolean dismissOnBackPressed) {
        isDismissOnBackPressed = dismissOnBackPressed;
    }
    public void setAutoOpenSoftInput(boolean autoOpenSoftInput) {
        isAutoOpenSoftInput = autoOpenSoftInput;
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(doAfterShowTask);
        removeCallbacks(doAfterDismissTask);
        if(showSoftInputTask != null){
            removeCallbacks(showSoftInputTask);
        }
    }

    private float x, y;
    private long downTime;
    private int touchSlop;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect rect = new Rect();
        getPopupContentView().getGlobalVisibleRect(rect);
        if(!PopupUtils.isInRect(event.getX(),event.getY(),rect)){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    downTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (distance < touchSlop && (System.currentTimeMillis() - downTime) < 350) {
                        if(isDismissOnTouchOutside){
                            dismiss();
                        }
                    }
                    x = 0;
                    y = 0;
                    downTime = 0;
                    break;
            }
        }
        return true;
    }

    // 生命周期
    protected abstract void onCreate();
    protected abstract void onShow();
    protected abstract void onDismiss();
    @NonNull
    protected abstract PopupAnimator getDefaultPopupAnimator();
    @LayoutRes
    protected abstract int getPopupViewLayoutId();
    @LayoutRes
    protected abstract int getImplLayoutId();
}
