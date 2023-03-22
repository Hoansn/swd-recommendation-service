package com.example.swd.task;

import com.example.swd.server.RecommendServiceHandler;
import com.example.swd.server.RecommendationSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PredictGradeTask implements Runnable  {
	static final Logger logger = LoggerFactory.getLogger(PredictGradeTask.class.getSimpleName());
    @Override
    public void run() {
		long startTime = System.nanoTime();
		int nUser = 100, nTest = 100;
		Random rand = new Random();
        ArrayList <ArrayList <Double>> userTestResult = new ArrayList<>();
		logger.info("== Predict Grade ==");
		logger.info("== Get Data: Processing ==");
        for (int i = 0; i < nUser; i++) {
			ArrayList <Double> grades = new ArrayList<>();
			for (int j = 0; j < nTest; j++) {
				if (rand.nextInt(10) != 0) {
					double grade = rand.nextInt(101);
					grades.add(grade / 10.0);
				} else {
					grades.add(-1.0);
				}
			}
			userTestResult.add(grades);
		}
		logger.info("== Get Data: Finish ==");

		/*System.out.println("Result:");
		for (int i = 0; i < nUser; i++) {
			System.out.println("" + userTestResult.get(i));
		}*/

		logger.info("== Preparing Data: Processing ==");
		HashMap <Integer, HashSet<Integer>> unTaken = new HashMap<>();
		for (int i = 0; i < nUser; i++) {
			unTaken.put(i, new HashSet<>());
			for (int j = 0; j < nTest; j++) {
				if (userTestResult.get(i).get(j) < 0)
					unTaken.get(i).add(j);
			}
		}
		ArrayList <Double> userAvg = new ArrayList<>();
		for (int i = 0; i < nUser; i++) {
			double sum = 0;
			int cnt = 0;
			for (int j = 0; j < nTest; j++) {
				if (!unTaken.get(i).contains(j)) {
					sum += userTestResult.get(i).get(j);
					cnt += 1;	
				}
			}
			double avg = 0;
			if (cnt > 0) avg = sum / cnt;
			userAvg.add(avg);
			for (int j = 0; j < nTest; j++) {
				double temp = userTestResult.get(i).get(j);
				if (!unTaken.get(i).contains(j))
					userTestResult.get(i).set(j, temp - userAvg.get(i));
				else 
					userTestResult.get(i).set(j, 0.0);
			}
		}
		logger.info("== Preparing Data: Finish ==");

		/*System.out.println("Result:");
		for (int i = 0; i < nUser; i++) {
			System.out.println("" + userTestResult.get(i));
		}*/

		logger.info("== Calculate: Processing ==");

		ArrayList <ArrayList <Double>> similars = new ArrayList<>();
		for (int i = 0; i < nUser; i++) {
			ArrayList <Double> similar = new ArrayList<>();
			for (int j = 0; j < nUser; j++) {
				if (i == j) {
					similar.add(1.0);
					continue;
				}
				double num = 0;
				double denX = 0;
				double denY = 0;
				for (int k = 0; k < nTest; k++) {
					num += userTestResult.get(i).get(k) * userTestResult.get(j).get(k);
					denX += Math.pow(userTestResult.get(i).get(k), 2);
					denY += Math.pow(userTestResult.get(j).get(k), 2);
				}
				double simValue = num / (Math.sqrt(denX) * Math.sqrt(denY));
				similar.add(simValue);
			}
			similars.add(similar);
		}
		
		/*System.out.println("Sim:");
		for (int i = 0; i < nUser; i++) {
			System.out.println("" + similars.get(i));
		}*/
		for (int i = 0; i < nUser; i++) {
			for (int j = 0; j < nTest; j++) {
				if (!unTaken.get(i).contains(j)) continue;
				double predict = 0;
				double num = 0;
				double den = 0;
				int cnt = 0;
				for (int k = 0; k < nUser; k++) {
					if (userTestResult.get(k).get(j) == 0) continue;
					num += userTestResult.get(k).get(j) * similars.get(i).get(k);
					den += Math.abs(similars.get(i).get(k));
					cnt += 1;
				}
				predict = num / den;
				userTestResult.get(i).set(j, predict);
			}
		}

		/*System.out.println("Result:");
		for (int i = 0; i < nUser; i++) {
			System.out.println("" + userTestResult.get(i));
		}*/

		for (int i = 0; i < nUser; i++) {
			for (int j = 0; j < nTest; j++) {
				double temp = userTestResult.get(i).get(j);
				int sign = 1;
				if (unTaken.get(i).contains(j)) sign = -1;
				userTestResult.get(i).set(j, sign * (temp + userAvg.get(i)));
			}
		}
		/*System.out.println("Result:");
		for (int i = 0; i < nUser; i++) {
			System.out.println("" + userTestResult.get(i));
		}*/

		logger.info("== Calculate: Finish ==");
		RecommendServiceHandler.INSTANCE.setMatrix(userTestResult);

		logger.info("== Predict Grade Finish ==");
		long endTime = System.nanoTime();
		long period = (endTime - startTime) / 1000000;
		logger.info("Time:  " + period + " milliseconds");
    }
}
