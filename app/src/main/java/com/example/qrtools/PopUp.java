package com.example.qrtools;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qr.core.popup.base.FullScreenPopupView;
import com.qr.core.popup.base.PopupViewConfig;

/**
 * Created by QR on 2019/4/29 10:43
 */
public class PopUp extends FullScreenPopupView {
    public PopUp(@NonNull Context context,PopupViewConfig popupViewConfig) {
        super(context,popupViewConfig);
    }

    @Override
    protected void onCreate() {

    }

    @Override
    protected void onShow() {

    }

    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.fragment_login;
    }
}
