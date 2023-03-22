/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.swd.client;

import com.example.swd.thrift.TRecommendService;
import java.util.List;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.*;

/**
 *
 * @author hoanns
 */
public class Client {
    
    private final String host;
    private final int port;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public Client (String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void ping() {
        TTransport transport = new TSocket(host, port);
        TRecommendService.Client client;
        try {
            transport.open();
            logger.info("Connected to {} on {}", host, port);
            TProtocol protocol = new TBinaryProtocol(transport);
            client = new TRecommendService.Client(protocol);
            int result = client.ping();
            logger.info("client.ping() = {}", result);
            transport.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }
	
	public List<Integer> getRecommendation(int userId, int targetId) {        
		TTransport transport = new TSocket(host, port);
        TRecommendService.Client client;
		List<Integer> result = null;
        try {
            transport.open();
            // logger.info("Connected to {} on {}", host, port);
            TProtocol protocol = new TBinaryProtocol(transport);
            client = new TRecommendService.Client(protocol);
			result = client.getRecommendation(userId, targetId);
            transport.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
			return result;
		}
    }
}
