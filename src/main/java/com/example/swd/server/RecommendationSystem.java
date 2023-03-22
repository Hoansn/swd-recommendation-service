/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package com.example.swd.server;

import com.example.swd.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hoanns
 */
public class RecommendationSystem {

	static final Logger logger = LoggerFactory.getLogger(RecommendationSystem.class.getSimpleName());
    
    public static void setupAndStart()
    {
        int port = 9090;
        logger.info("## Starting Server on port {} ##", port);
        final Server server = new Server(port);
        Thread serverThread = new Thread(server::doStart);
        serverThread.start();

        try { serverThread.sleep(1000); } catch (InterruptedException ie) {}
    }
    
    public static void printUsage() {
        System.out.println("java -jar thrift-example.jar [port] [first_number] [second_number]");
    }

	
}
