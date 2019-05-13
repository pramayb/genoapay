package com.genoapay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class GenoapayTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public GenoapayTest(String testName) {
		super(testName);
	}

	/**
	 * Test unordered stock prices
	 */
	public void testGetMaxProfit() {
		Integer[] stockPrices = { 8, 5, 11, 30, 10, 11, 35 };
		assertEquals(30, Genoapay.getMaxProfit(stockPrices));
	}

	/**
	 * Test stock prices ordered ascending
	 */
	public void testGetMaxProfitListAsc() {
		Integer[] stockPrices1 = { 1, 2, 3, 4, 5 };
		assertEquals(4, Genoapay.getMaxProfit(stockPrices1));
	}

	/**
	 * Test stock prices ordered descending
	 */
	public void testGetMaxProfitListDesc() {
		Integer[] stockPrices2 = { 5, 4, 3, 2, 1 };
		assertEquals(0, Genoapay.getMaxProfit(stockPrices2));
	}

	/**
	 * Test Stock prices with multiple valid invalid stock price inputs with respect
	 * to time they were purchased
	 */
	public void testGenoapay() {
		Map<LocalDateTime, Integer> stockPrices = new HashMap<LocalDateTime, Integer>();
		stockPrices.put(LocalDate.now().minusDays(1l).atTime(11, 00), 8); // yesterday 11:00 am
		stockPrices.put(LocalDate.now().minusDays(1l).atTime(12, 00), 5); // yesterday 12:00 pm
		stockPrices.put(LocalDate.now().minusDays(1l).atTime(13, 00), 11); // yesterday 1:00 pm
		stockPrices.put(LocalDate.now().minusDays(1l).atTime(9, 00), 30); // yesterday 9:00 am (before yesterdays
																			// trading hours)
		stockPrices.put(LocalDate.now().minusDays(1l).atTime(23, 38), 10); // yesterday 11:38 pm
		stockPrices.put(LocalDate.now().atTime(11, 53), 11); // today 11:53 am
		stockPrices.put(LocalDate.now().atTime(23, 53), 35); // today 11:53 pm

		assertEquals(6, Genoapay.getMaxProfit(stockPrices));
	}

}
