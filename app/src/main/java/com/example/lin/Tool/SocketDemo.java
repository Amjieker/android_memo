package com.example.lin.Tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SocketDemo {
    private OutputStream outputStream;
    private InputStream inputStream;
    Thread send = new Thread(new Runnable() {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while(true){
                String str = scanner.nextLine();
                try {
                    outputStream.write(str.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    Thread recv = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true){
                byte[] bytes = new byte[1024];
                try {
                    inputStream.read(bytes);
                    System.out.println(new String(bytes));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    Socket socket;

    public SocketDemo(String ip, int port){
        try{
            socket = new Socket(ip,port);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            send.start();
            recv.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SocketDemo("101.43.66.34",8086);
    }
}
