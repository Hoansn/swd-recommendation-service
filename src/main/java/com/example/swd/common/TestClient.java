package com.example.swd.common;
import com.example.swd.client.Client;
import org.slf4j.*;

import java.util.List;

public class TestClient {
    static final Logger logger = LoggerFactory.getLogger(TestClient.class.getSimpleName());
    public static void main(String args[]) {
        logger.info("## Simple Test ##");
        logger.info("## Starting Client ##");
        int port = 9090;
        Client client = new Client("localhost", port);
        client.ping();
        long startTime = System.nanoTime();
        List<Integer> testId = client.getRecommendation(0, 0);
        logger.info("Suggest: " + testId);
        long endTime = System.nanoTime();
        long period = (endTime - startTime) / 1000000;
        System.out.println("Time:  " + period + " milliseconds");
        logger.info("## Stop Client ##");
    }
}
