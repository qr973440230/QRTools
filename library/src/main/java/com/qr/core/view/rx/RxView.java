package com.qr.core.view.rx;

import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.CheckResult;

import io.reactivex.Observable;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RxView {
    @CheckResult
    public static Observable<View> getViewClickObservable(View view){
        return Observable.<View>create(emitter -> {
            view.setOnClickListener(v -> {
                if (!emitter.isDisposed()) {
                    emitter.onNext(view);
                }
            });
            emitter.setDisposable(new MainThreadDisposable() {
                @Override
                protected void onDispose() {
                    view.setOnClickListener(null);
                }
            });
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @CheckResult
    public static Observable<View> getViewLongClickObservable(View view){
        return Observable.<View>create(emitter -> {
            view.setOnLongClickListener(v -> {
                if(!emitter.isDisposed()){
                    emitter.onNext(v);
                    return true;
                }
                return false;
            });

            emitter.setDisposable(new MainThreadDisposable() {
                @Override
                protected void onDispose() {
                    view.setOnLongClickListener(null);
                }
            });
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @CheckResult
    public static Observable<Boolean> getViewFocusChangedObservable(View view){
        return Observable.<Boolean>create(emitter -> {
            view.setOnFocusChangeListener((v, hasFocus) -> {
                if(!emitter.isDisposed()){
                    emitter.onNext(hasFocus);
                }
            });
            emitter.setDisposable(new MainThreadDisposable() {
                @Override
                protected void onDispose() {
                    view.setOnFocusChangeListener(null);
                }
            });
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @CheckResult
    public static Observable<MotionEvent> getViewHoverEventObservable(View view){
        return Observable.<MotionEvent>create(emitter -> {
            view.setOnHoverListener((v,event)->{
                if(!emitter.isDisposed()){
                    emitter.onNext(event);
                    return true;
                }
                return false;
            });

            emitter.setDisposable(new MainThreadDisposable() {
                @Override
                protected void onDispose() {
                    view.setOnHoverListener(null);
                }
            });
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @CheckResult
    public static Observable<DragEvent> getViewDragEventObservable(View view){
        return Observable.<DragEvent>create(emitter -> {
            view.setOnDragListener((v, event) -> {
                if(!emitter.isDisposed()){
                    emitter.onNext(event);
                    return true;
                }
                return false;
            });

            emitter.setDisposable(new MainThreadDisposable() {
                @Override
                protected void onDispose() {
                    view.setOnDragListener(null);
                }
            });
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @CheckResult
    public static Observable<KeyEvent> getViewKeyEventObservable(View view){
        return Observable.<KeyEvent>create(emitter -> {
            view.setOnKeyListener((v, keyCode, event) -> {
                if(!emitter.isDisposed()){
                    emitter.onNext(event);
                    return true;
                }
                return false;
            });
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @CheckResult
    public static Observable<MenuItem> getMenuItemClickObservable(MenuItem menuItem){
        return Observable.<MenuItem>create(emitter -> {
            menuItem.setOnMenuItemClickListener(item -> {
                if(!emitter.isDisposed()){
                    emitter.onNext(item);
                    return true;
                }
                return false;
            });

            emitter.setDisposable(new MainThreadDisposable() {
                @Override
                protected void onDispose() {
                    menuItem.setOnMenuItemClickListener(null);
                }
            });
        }).subscribeOn(AndroidSchedulers.mainThread());
    }
}
