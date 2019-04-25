package com.example.qrtools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.qr.core.popup.animator.TranslateAlphaAnimator;
import com.qr.core.popup.impl.ConfirmPopupView;
import com.qr.core.zxing.CaptureActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View viewById = findViewById(R.id.btn);
//        ZXing
//        viewById.setOnClickListener(v -> {
//            Intent intent = new Intent(this, CaptureActivity.class);
//            startActivity(intent);
//        });

//        XPopup
        viewById.setOnClickListener(v -> {
            new ConfirmPopupView.Builder(this)
                    .title("Title")
                    .content("content")
                    .cancel(()->{
                        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                    })
                    .confirm(()->{
                        Toast.makeText(this, "Confirm", Toast.LENGTH_SHORT).show();
                    })
                    .build()
                    .show();
        });
    }

}
