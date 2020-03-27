package com.github.chenlijia1111.util.future;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * 测试java中的可回调异步任务 {@link java.util.concurrent.Future}
 *
 * @author Chen LiJia
 * @since 2020/3/22
 */
public class TestFuture {

    /**
     * 最基础的可回调异步任务就是 {@link java.util.concurrent.Callable}
     */
    @Test
    public void test1() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        //创建一个计数器,只有三个异步任务都做完了才继续往下
        CountDownLatch countDownLatch = new CountDownLatch(3);

        Future<String> future1 = threadPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                countDownLatch.countDown();
                return "我是任务1";
            }
        });
        Future<String> future2 = threadPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                countDownLatch.countDown();
                return "我是任务2";
            }
        });
        Future<String> future3 = threadPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                countDownLatch.countDown();
                return "我是任务3";
            }
        });


//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //任务都完成了,打印结果---不需要使用计数器 在进行调用get方法的时候会自动等待，如果还没有返回值会等待的
        try {
            System.out.println(future1.get() + future2.get() + future3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * java8  新增接口 {@link CompletableFuture} 他是继承自{@link Future} 的
     */
    @Test
    public void test2(){
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync((() -> {
            return "我是任务1";
        }));
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync((() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "我是任务2";
        }));
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync((() -> {
            return null;
        }));


        //任务都完成了,打印结果
        try {
            System.out.println(future1.get() + future2.get() + future3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
