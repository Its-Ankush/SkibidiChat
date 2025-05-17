package com.realtimechatapp.demo;
import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;


public class TestClass {

    public static void main(String[] args) throws IOException, InterruptedException {

//         this is just for testing various functions and code. Please ignore. This is for personal use

/*        String fileName="chat.html";
        try (InputStream is = TestClass.class.getClassLoader().getResourceAsStream("static/web/" + fileName)) {

            byte[] buffer = new byte[500];
            int bytesReadTillNow = 0;
            byte[] chunk;

            while ( (bytesReadTillNow = is.read(buffer)) !=-1 ) {
                if (bytesReadTillNow == -1) continue;
                chunk = Arrays.copyOf(buffer, bytesReadTillNow);
                System.out.println(new String(chunk));

            }
        }*/
        System.out.println("Hello from docker");
        System.out.println(System.getenv().getOrDefault("skibidi", "/bin/bash"));

    }

}
