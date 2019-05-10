package com.genoapay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Pramay
 * @version 1.0
 */
public class Genoapay {
	private final static LocalDateTime tradeOpeningTime = LocalDate.now().minusDays(1l).atTime(10, 00);

	/**
	 * get Max Profit for the stock prices and returns the best profit could have
	 * been made from 1 purchase and 1 sale of 1 stock.
	 * 
	 * @param stockPrices
	 * @return
	 */
	public static int getMaxProfit(Map<LocalDateTime, Integer> stockPrices) {
		if (stockPrices == null || stockPrices.size() <= 1)
			return 0;
		
		stockPrices = filterList(stockPrices);
		
		if(stockPrices.size() <= 1) {
			return 0;
		}

		List<Integer> values = new ArrayList<Integer>(preProcessList(stockPrices).values());
		return calculateMaxDifference(values);
	}

	private static Map<LocalDateTime, Integer> filterList(Map<LocalDateTime, Integer> stockPrices) {
		Map<LocalDateTime, Integer> collect = stockPrices.entrySet().stream()
				.filter(sp -> (sp.getKey().until(LocalDateTime.now(), ChronoUnit.MINUTES) > 1
						&& (tradeOpeningTime.until(sp.getKey(), ChronoUnit.MINUTES) > 1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return collect;
	}

	private static Map<Long, Integer> preProcessList(Map<LocalDateTime, Integer> stockPrices) {
		Map<Long, Integer> processedMap = new HashMap<Long, Integer>();
		for (Iterator<LocalDateTime> iterator = stockPrices.keySet().iterator(); iterator.hasNext();) {
			LocalDateTime localTime = iterator.next();
			processedMap.put(tradeOpeningTime.until(localTime, ChronoUnit.MINUTES), stockPrices.get(localTime));
		}

		Map<Long, Integer> collect = processedMap.entrySet().stream()
				.sorted(Map.Entry.comparingByKey(Comparator.naturalOrder())).collect(Collectors.toMap(Map.Entry::getKey,
						Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		return collect;
	}

	private static int calculateMaxDifference(List<Integer> values) {
		int maxDiff = 0;
		for (int i = 0; i < values.size(); i++) {
			for (int j = i + 1; j < values.size(); j++) {
				int valDiff = values.get(j) - values.get(i);
				if (valDiff > maxDiff) {
					maxDiff = valDiff;
				}
			}
		}

		return maxDiff;
	}

}
