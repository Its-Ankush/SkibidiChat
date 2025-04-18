package com.realtimechatapp.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StaticFileServer {

//            new ResponseHandler().sendResponse(resp, 400, "No login page for you!");


    public static void serveStaticFile(HttpServletResponse resp, String fileName) throws ServletException, IOException {

        InputStream is = StaticFileServer.class.getClassLoader().getResourceAsStream("static/web/" + fileName);
        if (is==null){
            new ResponseHandler().sendResponse(resp, 404, "Could not find login page to serve");
            return;
        }
        int bytesReadTillNow=0;
        byte[] buffer= new byte[500];
        OutputStream outputStream=resp.getOutputStream();
        resp.setContentType("text/html");

//https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
        try {
            while ( (bytesReadTillNow=is.read(buffer))!=-1 ){
                outputStream.write(buffer,0,bytesReadTillNow);
            }
        } catch (IOException e) {
            is.close();
            outputStream.close();
            throw new RuntimeException(e);

        }

    }
}
