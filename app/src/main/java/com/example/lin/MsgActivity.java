package com.example.lin;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MsgActivity extends Activity implements Runnable {
    private PrintWriter cout;
    private InputStream cin;
    private Socket socket;
    private String msg_str;
    private TextView input;
    private Thread recv;
    private Boolean recv_flag;
    private ArrayList<String> list = new ArrayList<String>();
    private StringBuilder builder_str = new StringBuilder("");
    private String[] permissions = new String[]{
            "android.permission.INTERNET"
    };
    public Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x133) {
//                builder_str.append(msg_str);
                ((TextView) findViewById(R.id.show_msg)).setText(builder_str);
//                list.add(msg_str);
                ((ScrollView) findViewById(R.id.scrol)).fullScroll(View.FOCUS_DOWN);
                input.setText("");
//                ((ListView) findViewById(R.id.show_msg)).setAdapter(new ArrayAdapter<String>(
//                        MsgActivity.this, R.layout.support_simple_spinner_dropdown_item, list));
//                System.out.println(msg_str);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActivityCompat.requestPermissions(this,permissions,permissions.length);
//        list.add(" ");
//        ((ListView) findViewById(R.id.show_msg)).setAdapter(new ArrayAdapter<String>(
//                MsgActivity.this, R.layout.support_simple_spinner_dropdown_item, list));
        input = findViewById(R.id.input);
        recv = new Thread(MsgActivity.this);
        // 临时线程，因为主线程中是UI线程，网络请求也不能开在主线程
        new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket("101.43.66.34", 8086);
//                    cin = new BufferedReader(
//                            new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    cin = socket.getInputStream();
                    cout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream()
                    )), true);
//                    cout.print("link");
//                    cout.flush();
                    cout.print(getIntent().getStringExtra("account"));
                    cout.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        findViewById(R.id.submit).setOnClickListener(v -> new Thread() {
            @Override
            public void run() {
                try {
                    String msg = input.getText().toString();
//                            System.out.println("first "+msg);
                    if (socket.isConnected() && !socket.isOutputShutdown())
//                                System.out.println("msg :"+msg);
                    {
                        cout.print(msg);
                        cout.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start());
        recv_flag = true;
        recv.start();
    }

    @Override
    public void run() {
        try {
            while (true && recv_flag) {
                if (cin == null) continue;
                byte[] bytes = new byte[1024];
                int len = cin.read(bytes);
                if (len != -1) {
                    builder_str.append(new String(bytes, 0, len, "UTF-8") + "\n");
                    handler.sendEmptyMessage(0x133);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            // 线程
            new Thread(() -> {
                try {
                    recv_flag = false;
                    cin.close();
                    cout.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
