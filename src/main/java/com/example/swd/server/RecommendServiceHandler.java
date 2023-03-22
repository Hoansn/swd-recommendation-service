/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.swd.server;

import com.example.swd.thrift.TRecommendService;
import java.util.*;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hoanns
 */
public class RecommendServiceHandler implements TRecommendService.Iface {
	private static final Logger logger = LoggerFactory.getLogger(RecommendServiceHandler.class);

	public static final RecommendServiceHandler INSTANCE = new RecommendServiceHandler();

	public String temp = "";
	private ArrayList <ArrayList <Double>> matrix = new ArrayList<>();
	public void init() {
		temp = "Hello world!!!";
	}

	public void setMatrix(ArrayList <ArrayList <Double>> matrix) {
		this.matrix = matrix;
	}
	@Override
	public int ping() throws TException {
		System.out.println("Ping!!!");
		return 1;
		//throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

	@Override
	public ArrayList<Integer> getRecommendation(int userId, int targetId) throws TException {
		ArrayList<Integer> recommends = new ArrayList<>();
		int nUser = matrix.size();
		int nTest = matrix.get(0).size();
		ArrayList <ArrayList <Double>> uidResult = new ArrayList<>();
		for (int i = 0; i < nTest; i++) {
			if (matrix.get(userId).get(i) < 0) {
				uidResult.add(new ArrayList<>(
						Arrays.asList(-matrix.get(userId).get(i), (double)i))
				);
			}
		}

		Collections.sort(uidResult, new Comparator<ArrayList<Double>> () {
			@Override
			public int compare(ArrayList<Double> a, ArrayList<Double> b) {
				return b.get(0).compareTo(a.get(0));
			}
		});
		for (int i = 0; i < uidResult.size(); i++) {
			double grade = uidResult.get(i).get(0);
			int testId = (int)Math.round(uidResult.get(i).get(1));
			recommends.add(testId);
			logger.info("Test: " + testId + "; Expect result: " + grade);
		}
		logger.info("User: " +  userId + " Result: " + uidResult);
		return recommends;
	}


}
