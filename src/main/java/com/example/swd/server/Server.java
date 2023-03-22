/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.example.swd.server;

import com.example.swd.task.PredictGradeTask;
import com.example.swd.thrift.TRecommendService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    private final int port;
    private TServer server = null;
    private final TRecommendService.Processor<RecommendServiceHandler> processor;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ScheduledExecutorService _executorService = Executors.newSingleThreadScheduledExecutor();
    public Server(int p) {
        port = p;
        processor = new TRecommendService.Processor(RecommendServiceHandler.INSTANCE);
        _executorService.scheduleAtFixedRate(new PredictGradeTask(), 0, 1, TimeUnit.DAYS);
    }

    public void doShutdown() {
        if (server != null) {
            server.stop();
        }
        server = null;
    }

    public void doStart() {
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            //TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

            server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            logger.info("Server is going live");
            server.serve();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
