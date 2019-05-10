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
	
	//Field to store previous day's trade opening time ie. Yesterday at 10.00 am
	private final static LocalDateTime tradeOpeningTime = LocalDate.now().minusDays(1l).atTime(10, 00);

	/**
	 * get Max Profit for the stock prices and returns the best profit could have
	 * been made from 1 purchase and 1 sale of 1 stock.
	 * 
	 * @param stockPrices
	 * @return max profit calculated
	 */
	public static int getMaxProfit(Map<LocalDateTime, Integer> stockPrices) {
		//check for valid stock prices input
		if (stockPrices == null || stockPrices.size() <= 1)
			return 0;

		stockPrices = filterList(stockPrices);

		//check valid stock price list after applying filter
		//If size is less than or equal than 1 it makes no criteria for calculation
		if (stockPrices.size() <= 1) {
			return 0;
		}

		List<Integer> values = new ArrayList<Integer>(processPriceList(stockPrices).values());
		return calculateMaxProfit(values);
	}

	/**
	 * Filtering added so as to skip invalid time range
	 * 
	 * @param stockPrices
	 * @return filtered map of stocks
	 */
	private static Map<LocalDateTime, Integer> filterList(Map<LocalDateTime, Integer> stockPrices) {
		return stockPrices.entrySet().stream()
				//check for past and future stock purchases, if found any skip it
				.filter(sp -> (sp.getKey().until(LocalDateTime.now(), ChronoUnit.MINUTES) > 1
						&& (tradeOpeningTime.until(sp.getKey(), ChronoUnit.MINUTES) > 1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * To process stock prices they need to be sorted as per their time series of
	 * purchase
	 * 
	 * @param stockPrices
	 * @return
	 */
	private static Map<Long, Integer> processPriceList(Map<LocalDateTime, Integer> stockPrices) {
		Map<Long, Integer> processedStockPrices = new HashMap<Long, Integer>();
		
		//store stock prices with the time difference of between self and trade opening time
		for (Iterator<LocalDateTime> iterator = stockPrices.keySet().iterator(); iterator.hasNext();) {
			LocalDateTime localTime = iterator.next();
			processedStockPrices.put(tradeOpeningTime.until(localTime, ChronoUnit.MINUTES), stockPrices.get(localTime));
		}

		//sort stock prices on the basis of their purchase time line with the help of Minute difference from trade opening time
		Map<Long, Integer> sortedStockPrices = processedStockPrices.entrySet().stream()
				.sorted(Map.Entry.comparingByKey(Comparator.naturalOrder())).collect(Collectors.toMap(Map.Entry::getKey,
						Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		return sortedStockPrices;
	}

	/**
	 * Max Stock profit is calculated with the difference between two stock purchases in time series
	 * 
	 * @param stockPrices
	 * @return max profit calculated
	 */
	private static int calculateMaxProfit(List<Integer> stockPrices) {
		int maxProfit = 0;
		for (int i = 0; i < stockPrices.size(); i++) {
			for (int j = i + 1; j < stockPrices.size(); j++) {
				int stockDiff = stockPrices.get(j) - stockPrices.get(i);
				if (stockDiff > maxProfit) {
					maxProfit = stockDiff;
				}
			}
		}

		return maxProfit;
	}

}
