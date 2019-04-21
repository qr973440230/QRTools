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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        adapter.addModel(new HeaderModel(""));
        adapter .addModel(new ImageModel(""));
        adapter .addModel(new ImageModel(""));
        adapter.addModel(new BtnModel(""));
        adapter.addModel(new BtnModel(""));
        adapter.addModel(new BtnModel(""));
        adapter .addModel(new ImageModel(""));
        adapter .addModel(new ImageModel(""));
        adapter .addModel(new ImageModel(""));
        adapter.addModel(new BtnModel(""));
        adapter.addModel(new BtnModel(""));
        adapter .addModel(new ImageModel(""));
        adapter .addModel(new ImageModel(""));
        adapter.addModel(new BtnModel(""));
        adapter.addModel(new BtnModel(""));
        adapter.addModel(new FooterModel(""));
    }

    public class ImageModel extends RecyclerViewModel<String>{
        public ImageModel(String data) {
            super(data);
        }

        @Override
        public int spinSize() {
            return 1;
        }

        @Override
        public int layoutId() {
            return R.layout.item_test2;
        }

        @Override
        public void bindView(RecyclerViewViewHolder holder, final int position, List<Object> payloads) {
            holder.setOnClickListener(R.id.image, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "On Click" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public class BtnModel extends RecyclerViewModel<String>{
        public BtnModel(String data) {
            super(data);
        }

        @Override
        public int spinSize() {
            return 1;
        }

        @Override
        public int layoutId() {
            return R.layout.item_test;
        }

        @Override
        public void bindView(RecyclerViewViewHolder holder, final int position, List<Object> payloads) {
            holder.setOnClickListener(R.id.item_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "On Click" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public class HeaderModel extends RecyclerViewModel<String> {
        public HeaderModel(String data) {
            super(data);
        }

        @Override
        public int spinSize() {
            return 4;
        }

        @Override
        public int layoutId() {
            return R.layout.item_test4;
        }

        @Override
        public void bindView(RecyclerViewViewHolder holder, final int position, List<Object> payloads) {
            holder.setOnClickListener(R.id.container, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "On Click" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public class FooterModel extends RecyclerViewModel<String>{
        public FooterModel(String data) {
            super(data);
        }

        @Override
        public int spinSize() {
            return 4;
        }

        @Override
        public int layoutId() {
            return R.layout.item_test3;
        }

        @Override
        public void bindView(RecyclerViewViewHolder holder, int position, List<Object> payloads) {
            holder.setOnLongClickListener(R.id.container, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(MainActivity.this, "On Long Click", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }
}
