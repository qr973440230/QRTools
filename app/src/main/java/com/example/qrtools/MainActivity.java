package com.example.qrtools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.qr.core.tools.adapter.recyclerview.RecyclerViewAdapter;
import com.qr.core.tools.adapter.recyclerview.RecyclerViewModel;
import com.qr.core.tools.adapter.recyclerview.RecyclerViewViewHolder;
import com.qr.core.tools.popup.impl.ConfirmPopupView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View viewById = findViewById(R.id.btn);
        viewById.setOnClickListener(v -> {
            new ConfirmPopupView.Builder(this)
                    .title("title")
                    .content("content")
                    .cancel(()->{
                        Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
                    })
                    .confirm(()->{
                        Toast.makeText(this, "confirm", Toast.LENGTH_SHORT).show();
                    })
                    .build()
                    .show();
        });
    }

}
