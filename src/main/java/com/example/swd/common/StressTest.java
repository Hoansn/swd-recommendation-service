package com.example.swd.common;

import com.example.swd.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StressTest implements Runnable {
    static final Logger logger = LoggerFactory.getLogger(StressTest.class.getSimpleName());
    public static void main(String args[]) throws InterruptedException {
        // logger.info("## Stress Test ##");

        long startTime = System.nanoTime();
        StressTest test = new StressTest();
        int nThread = 10;
        Thread[] thread = new Thread[nThread];
        for (int i = 0; i < nThread; i++) {
            thread[i] = new Thread(test, "Stress Test " + i);
            thread[i].start();
        }
        int total = 0;
        for (int i = 0; i < nThread; i++) {
            thread[i].join();
        }
        long endTime = System.nanoTime();
        long period = (endTime - startTime) / 1000000;
        System.out.println("Time:  " + period + " milliseconds");
        // logger.info("## Stop Client ##");
    }

    @Override public void run()
    {
        long startTime = System.nanoTime();
        // logger.info("## Starting Client " + Thread.currentThread().getName() + "##");
        int port = 9090;
        int cnt = 0;
        Client client = new Client("localhost", port);
        while ((System.nanoTime() - startTime) < 1000 * 1000000)   {
            List<Integer> testId = client.getRecommendation(0, 0);
            cnt += 1;
        }
        logger.info("Client " + Thread.currentThread().getName() + " make " + cnt + " req/s");
        // logger.info("## Stop Client " + Thread.currentThread().getName() + "##");
    }
}
