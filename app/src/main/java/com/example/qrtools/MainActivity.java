package com.example.qrtools;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.qr.core.popup.impl.ConfirmPopupView;
import com.qr.core.popup.impl.InputConfirmPopupView;
import com.qr.core.popup.util.PopupUtils;
import com.qr.core.utils.WindowUtils;


public class MainActivity extends AppCompatActivity {
    public static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        View viewById1 = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
//        viewById1.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        Log.d(TAG, String.valueOf(viewById1.getSystemUiVisibility()& View.SYSTEM_UI_FLAG_HIDE_NAVIGATION));
        View viewById = findViewById(R.id.btn);
//        viewById.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

//        ZXing
//        viewById.setOnClickListener(v -> {
//            Intent intent = new Intent(this, CaptureActivity.class);
//            startActivity(intent);
//        });

//        XPopup
        viewById.setOnClickListener(v -> {

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
