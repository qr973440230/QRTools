package com.qr.core.popup.base;

import com.qr.core.popup.animator.PopupAnimator;

public class PopupViewConfig {
    PopupAnimator popupAnimator = null;
    boolean isShadowBackground = true;
    boolean isDismissOnTouchOutside = true;
    boolean isRequestFocus = true;
    boolean isDismissOnBackPressed = true;
    boolean isAutoOpenSoftInput = false;
    boolean isAutoMoveToKeyboard = false;

    public PopupViewConfig setPopupAnimator(PopupAnimator popupAnimator) {
        this.popupAnimator = popupAnimator;
        return this;
    }

    public PopupViewConfig setShadowBackground(boolean shadowBackground) {
        isShadowBackground = shadowBackground;
        return this;
    }

    public PopupViewConfig setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        isDismissOnTouchOutside = dismissOnTouchOutside;
        return this;
    }

    public PopupViewConfig setRequestFocus(boolean requestFocus) {
        isRequestFocus = requestFocus;
        return this;
    }

    public PopupViewConfig setDismissOnBackPressed(boolean dismissOnBackPressed) {
        isDismissOnBackPressed = dismissOnBackPressed;
        return this;
    }

    public PopupViewConfig setAutoOpenSoftInput(boolean autoOpenSoftInput) {
        isAutoOpenSoftInput = autoOpenSoftInput;
        return this;
    }

    public PopupViewConfig setAutoMoveToKeyboard(boolean autoMoveToKeyboard) {
        isAutoMoveToKeyboard = autoMoveToKeyboard;
        return this;
    }
}
