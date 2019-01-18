package com.nowcoder;

/**
 * @Author: pyh
 * @Date: 2019/1/17 15:32
 * @Version 1.0
 * @Function:   多线程功能测试
 */
class MyThread extends Thread{
    private int tid;

    public MyThread(int tid){
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for(int i = 0; i < 10; i++){
                Thread.sleep(1000);
                System.out.println(String.format("%d : %d", tid,i));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class MultiThreadTests {

    public static void testThread(){
        for(int i = 0; i < 10; i++){
            new MyThread(i).start();
        }
    }

    public static void main(String[] args){
        testThread();
    }

}
