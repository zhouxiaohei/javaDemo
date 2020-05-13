package com.zhou.threadDemo.CompletableFutureDemo;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName CompletableFutureDemoTwo
 * @Author JackZhou
 * @Date 2020/5/10  21:07
 **/
public class CompletableFutureDemoTwo {

    //thenAccept/thenAcceptAsync  对结果进行处理  参是Consumer  有入参无返回值;
    //对比thenCompose  是无返回值，只对结果进行消费
    public void testThenAccept(){
        CompletableFuture.supplyAsync(() -> "hello").thenAccept(s -> System.out.println(s + " world"));
    }

    //thenAcceptBoth  对两个结果进行消费
    public void testthenAcceptBoth() {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "world";
        }), (s1, s2) -> System.out.println(s1 + " " + s2));
    }

    // runAfterBoth  只关心这两个CompletionStage执行完毕，之后在进行操作  无返回值
    // 对比 thenRunAsync;单个和double
    public void testRunAfterBoth(){
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s1";
        }).runAfterBothAsync(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s2";
        }), () -> System.out.println("hello world"));
    }

    // 谁计算的快，我就用那个CompletionStage的结果进行下一步的转化操作。
    //applyToEither/applyToEitherAsync
    public void testApplyToEither() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s1";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello world";
        }), s -> "组合" + s).join();
        System.out.println(result);
    }

    //谁计算的快，我就用那个CompletionStage的结果进行下一步的消耗操作。
    //acceptEither/acceptEitherAsync
    public void testAcceptEither() {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s1";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello world";
        }), s -> System.out.println(s));
    }

    //两个CompletionStage，任何一个完成了都会执行下一步的操作（Runnable）。
    //runAfterEither/runAfterEitherAsync
    public void testRunAfterEither() {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s1";
        }).runAfterEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s2";
        }), () -> System.out.println("hello world"));
    }

    //当运行时出现了异常，可以通过exceptionally进行补偿
    //exceptionally
    public void testExceptionally() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return "hello world";
        }).join();
        System.out.println(result);
    }

    /**
        结果  这里也可以看出，如果使用了exceptionally，就会对最终的结果产生影响，它没有口子返回如果没有异常时的正确的值
        null
       java.lang.RuntimeException: 测试一下异常情况
       java.lang.RuntimeException: 测试一下异常情况
       hello world
     **/
    public void testWhenComplete() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).whenComplete((s, t) -> {
            System.out.println(s);
            System.out.println(t.getMessage());
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return "hello world";
        }).join();
        System.out.println(result);
    }

    // handle 异常已返回handle的值，正常会返回正常值
    // 结果 hello world
    public void testHandle1() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //出现异常
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).handle((s, t) -> {
            if (t != null) {
                return "hello world";
            }
            return s;
        }).join();
        System.out.println(result);
    }

    //结果 s1
    public void testHandle2() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //出现异常
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).handle((s, t) -> {
            if (t != null) {
                return "hello world";
            }
            return s;
        }).join();
        System.out.println(result);
    }
}
