package com.example.lin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // 处理背景透明度
        findViewById(R.id.login_con).getBackground().setAlpha(150);
        findViewById(R.id.button_login).setOnClickListener(V -> {
            Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
        });
    }
    
}