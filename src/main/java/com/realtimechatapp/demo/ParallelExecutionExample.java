package com.realtimechatapp.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelExecutionExample {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture completableFuture1 = CompletableFuture.runAsync(() -> {
            int sum = 0;
            for (int i = 0; i < 1000; i++) {
                sum += i;
            }
            System.out.println("First Sum is " + sum);
            System.out.println(Thread.currentThread().getName());

        }, executor).thenRun(() -> {
            System.out.println("First sum added and done");
        });

        CompletableFuture completableFuture2 = CompletableFuture.runAsync(() -> {
            int sum = 0;
            for (int i = 0; i < 1000; i++) {
                sum += i;
            }
            System.out.println("Second Sum is " + sum);
            System.out.println(Thread.currentThread().getName());


        }, executor).thenRun(() -> {
            System.out.println("Second sum added and done");

        });

        CompletableFuture.allOf(completableFuture1, completableFuture2).thenRun(() -> {
            System.out.println("Both tasks done and now closing executor");
            executor.shutdown();
        });

        System.out.println("Printing from main  thread");
        System.out.println(Thread.currentThread().getName());



    }
}
