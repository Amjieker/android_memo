package com.example.lin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lin.Tool.Const;
import com.example.lin.Tool.ListAdapter;
import com.example.lin.Tool.Tools;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class MsgFragment extends Fragment implements Runnable {
    private PrintWriter cout;
    private InputStream cin;
    private Date date;
    private Socket socket;
    private String msg_str;
    private TextView input;
    private View view;
    private ListAdapter listAdapter;
    private Context context;
    private Thread recv;
    private Boolean recv_flag;
    private Boolean change_flag;
    private LinkedList<String> list = new LinkedList<>();
    private StringBuilder builder_str = new StringBuilder("");
    private final String[] permissions = new String[]{
            "android.permission.INTERNET"
    };
    public Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x133) {
                listAdapter.notifyDataSetChanged();
                ((ListView) view.findViewById(R.id.show_list))
                        .setAdapter(listAdapter);
//                ((ScrollView) findViewById(R.id.scrol))
//                        .fullScroll(View.FOCUS_DOWN);
//                恢复空白
                input.setText("");
//                ((ListView) findViewById(R.id.show_msg)).setAdapter(new ArrayAdapter<String>(
//                        MsgActivity.this, R.layout.support_simple_spinner_dropdown_item, list));
//                System.out.println(msg_str);
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.main_msg,container,false);
        date = new Date();
        Const.name = getActivity().getIntent().getStringExtra("account");
        this.context = getActivity();
        listAdapter = new ListAdapter(context, list, Const.name);
        ((ListView) view.findViewById(R.id.show_list)).setAdapter(listAdapter);
//        ActivityCompat.requestPermissions(this,permissions,permissions.length);
//        list.add(" ");
//        ((ListView) findViewById(R.id.show_msg)).setAdapter(new ArrayAdapter<String>(
//                MsgActivity.this, R.layout.support_simple_spinner_dropdown_item, list));
        input = view.findViewById(R.id.input);
        recv = new Thread(this);
        // 临时线程，因为主线程中是UI线程，网络请求也不能开在主线程
        new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket(Const.IP, Const.Port);
//                    cin = new BufferedReader(
//                            new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    cin = socket.getInputStream();
                    cout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream()
                    )), true);
//                    cout.print("link");
//                    cout.flush();
                    cout.print(Const.Key + Const.name);
                    cout.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        view.findViewById(R.id.submit).setOnClickListener(v -> new Thread() {
            @Override
            public void run() {
                if (new Date().getTime()-date.getTime()>=500) {
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
                }else{
                    Tools.Toast(context,"点慢一点哦，太快了");
                }
            }
        }.start());
        recv_flag = true;
        recv.start();
        return view;
    }

    @Override
    public void run() {
        try {
            while (true && recv_flag) {
                if (cin == null || !socket.isConnected()) continue;
//                byte[] bytes = new byte[1024];
                int len = cin.read();
                if (len != -1) {
//                    builder_str.append(new String(bytes, 0, len, "UTF-8") + "\n");
                    int x = cin.read();
                    int y = cin.read();
                    len = len * 256 + x;
                    byte[] bytes = new byte[len];
                    cin.read(bytes);
//                    for (byte i : bytes) {
//                        System.out.printf("%h ", i);
//                    }
//                    System.out.println("len: " + len + " y " + y);
//                    System.out.println(new String(bytes, 0, len, "UTF-8"));
                    list.add(new String(bytes, 0, len, "UTF-8"));
                    handler.sendEmptyMessage(0x133);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
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
