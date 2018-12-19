package com.nowcoder;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

public class MultiThreadTests {

    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void testExecutors() {
        //ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit( () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("Executor1:" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        service.submit( () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("Executor2:" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        service.shutdown();

        while (!service.isTerminated()) {
            try {
                Thread.sleep(1000);
                System.out.println("Wait for terminate.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //线程内的独立变量
    public static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    public static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(() -> {
                try {
//                    threadLocalUserIds.set(finalI);
                    Thread.sleep(1000);
//                    System.out.println("ThreadLocal:" + threadLocalUserIds.get());
                    userId = finalI;
                    System.out.println("userId:" + userId);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        // testThead();
        // testSynchronized();
        // testBlockingQueue();
        //testThreadLocal();
        testExecutors();
    }

    public static void testThead() {
        // extends Thead, 重载run()方法
//        for (int i = 0; i < 10; i++) {
//            new MyThread(i).start();
//        }

        // implements Runnable, 实现run()方法
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            System.out.println(String.format("T2 : %d", i));
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static final Object obj = new Object();
    // Synchronized
    public static void testSynchronized1() {
        synchronized(obj) {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4 : %d", i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized(obj) {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3 : %d", i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void testSynchronized() {
        for (int i = 0; i < 10; i++) {
            // lambda匿名方法
            new Thread(() -> {
                        testSynchronized1();
                        testSynchronized2();
                    }).start();
        }
    }


    public static void testBlockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "Consumer1").start();
        new Thread(new Consumer(q), "Consumer2").start();
    }
}

class MyThread extends Thread {

    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        // fori快捷键
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println(String.format("%d : %d", tid, i));
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Consumer implements Runnable {
    private BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + queue.take());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> queue;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(100);
                queue.put(String.valueOf(i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
