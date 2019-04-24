package com.example.qrtools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qr.core.zxing.CaptureActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View viewById = findViewById(R.id.btn);
        viewById.setOnClickListener(v -> {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivity(intent);
        });
    }

}
