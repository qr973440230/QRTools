package com.example.qrtools;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.qr.core.view.popup.impl.SpinKitLoading;

public class MainActivity extends AppCompatActivity {
    public static String TAG = MainActivity.class.getName();
    SpinKitLoading spinKitLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinKitLoading == null){
                    spinKitLoading = new SpinKitLoading(MainActivity.this)
                            .setTitle("正在加载中...");
                    spinKitLoading.show();
                }else{
                    spinKitLoading.dismiss();
                    spinKitLoading = null;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
