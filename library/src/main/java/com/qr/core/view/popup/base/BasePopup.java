package com.qr.core.view.popup.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.qr.core.view.popup.XPopup;
import com.qr.core.view.popup.animator.PopupAnimator;
import com.qr.core.view.popup.animator.ShadowBgAnimator;
import com.qr.core.view.popup.enums.PopupStatus;

import java.util.ArrayList;

public abstract class BasePopup extends FrameLayout {
    public static final String TAG = BasePopup.class.getName();
    private int touchSlop;
    protected ShadowBgAnimator shadowBgAnimator;
    protected PopupAnimator popupAnimator;
    protected ViewGroup layoutView;
    protected ViewGroup decorView;
    protected PopupStatus popupStatus;
    protected PopupInfo popupInfo;

    public BasePopup(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BasePopup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BasePopup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    public void show(){
        if(popupStatus != PopupStatus.Dismiss){
            return;
        }
        // 进入动画状态
        popupStatus = PopupStatus.Showing;

        Activity activity = ActivityUtils.getActivityByContext(getContext());
        KeyboardUtils.registerSoftInputChangedListener(activity, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                BasePopup.this.onSoftInputChanged(height);
            }
        });

        decorView = (ViewGroup) activity.getWindow().getDecorView();
        decorView.post(new Runnable() {
            @Override
            public void run() {
                if(getParent() != null){
                    ((ViewGroup)getParent()).removeView(BasePopup.this);
                }
                decorView.addView(BasePopup.this,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));

                init();
            }
        });
    }
    public void dismiss(){
        if(popupStatus != PopupStatus.Show){
            return;
        }
        popupStatus = PopupStatus.Dismissing;
        doDismissAnimation();
        doAfterDismiss();
    }

    protected void doShowAnimation() {
        if(popupInfo.hasShadowBg) {
            shadowBgAnimator.animateShow();
        }
        if(popupAnimator != null){
            popupAnimator.animateShow();
        }
    }
    protected void doDismissAnimation() {
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.animateDismiss();
        }
        if (popupAnimator != null)
            popupAnimator.animateDismiss();
    }

    protected Runnable doAfterShowTask = new Runnable() {
        @Override
        public void run() {
            popupStatus = PopupStatus.Show;
            focusAndProcessBackPress();
        }
    };
    protected Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            if(popupInfo.isRequestFocus) {
                Activity activity = ActivityUtils.getActivityByContext(getContext());
                View contentView = activity.findViewById(android.R.id.content);
                contentView.setFocusable(true);
                contentView.setFocusableInTouchMode(true);
            }

            // 移除弹窗，GameOver
            decorView.removeView(BasePopup.this);
        }
    };
    protected void doAfterShow(){
        removeCallbacks(doAfterShowTask);
        postDelayed(doAfterShowTask, XPopup.getAnimationDuration());
    }
    protected void doAfterDismiss() {
        if(popupInfo.isRequestFocus)
            KeyboardUtils.hideSoftInput(this);
        removeCallbacks(doAfterDismissTask);
        postDelayed(doAfterDismissTask, XPopup.getAnimationDuration());
    }

    protected void onSoftInputChanged(int height){
        LogUtils.dTag(TAG,"SoftInputHeightChanged: ",height);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(doAfterShowTask);
        removeCallbacks(doAfterDismissTask);
        if(showSoftInputTask != null){
            removeCallbacks(showSoftInputTask);
            showSoftInputTask = null;
        }

        popupStatus = PopupStatus.Dismiss;
    }

    private float x, y;
    private long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果自己接触到了点击，并且不在PopupContentView范围内点击，则进行判断是否是点击事件
        // 如果是，则dismiss
        Rect rect = new Rect();
        layoutView.getGlobalVisibleRect(rect);
        if (!XPopup.isInRect(event.getX(), event.getY(), rect)) {
            switch (event.getAction()) {
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
                        if (popupInfo.isDismissOnTouchOutside)
                            dismiss();
                    }
                    x = 0;
                    y = 0;
                    downTime = 0;
                    break;
            }
        }
        return true;
    }

    // 抽象函数
    protected abstract int getPopupLayoutId();
    protected abstract PopupAnimator getPopupAnimator();

    private void initView(Context context){
        layoutView = (ViewGroup) LayoutInflater.from(context).inflate(getPopupLayoutId(), this, false);
        layoutView.setAlpha(0);
        addView(layoutView);

        shadowBgAnimator = new ShadowBgAnimator(this);
        popupAnimator = getPopupAnimator();
        popupAnimator.initAnimator();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private void init(){
        post(new Runnable() {
            @Override
            public void run() {
                Activity activity = ActivityUtils.getActivityByContext(getContext());
                FrameLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                if(BarUtils.isStatusBarVisible(activity)){
                    // 不要遮住状态栏
                    layoutParams.topMargin = BarUtils.getStatusBarHeight();
                }
                if(BarUtils.isNavBarVisible(activity)){
                    // 不要遮住导航栏
                    layoutParams.bottomMargin = BarUtils.getNavBarHeight();
                }
                setLayoutParams(layoutParams);

                layoutView.setAlpha(1.0f);
                doShowAnimation();
                doAfterShow();
            }
        });
    }

    private ShowSoftInputTask showSoftInputTask;
    private void focusAndProcessBackPress() {
        // 处理返回按键
        if(popupInfo.isRequestFocus){
            setFocusableInTouchMode(true);
            requestFocus();
        }
        // 此处焦点可能被内容的EditText抢走，也需要给EditText也设置返回按下监听
        setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (popupInfo.isDismissOnBackPressed){
                        dismiss();
                    }
                    return true;
                }
                return false;
            }
        });

        //let all EditText can process back pressed.
        ArrayList<EditText> list = new ArrayList<>();
        XPopup.findAllEditText(list, layoutView);
        for (int i = 0; i < list.size(); i++) {
            final View et = list.get(i);
            if (i == 0) {
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocus();
                if (popupInfo.autoOpenSoftInput) {
                    if(showSoftInputTask == null){
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
                        if (popupInfo.isDismissOnBackPressed)
                            dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

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
}
