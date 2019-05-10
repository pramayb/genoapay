package com.genoapay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class GenoapayTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GenoapayTest( String testName )
    {
        super( testName );
    }

    /**
     * Test Stock prices with multiple valid invalid stock price inputs
     * 
     */
    public void testGenoapay()
    {
    	Map<LocalDateTime, Integer> stockPrices = new HashMap<LocalDateTime, Integer>();
    	stockPrices.put(LocalDate.now().minusDays(1l).atTime(11, 00), 8); // yesterday 11:00 am
    	stockPrices.put(LocalDate.now().minusDays(1l).atTime(12, 00), 5); // yesterday 12:00 pm
    	stockPrices.put(LocalDate.now().minusDays(1l).atTime(13, 00), 11); // yesterday 1:00 pm
    	stockPrices.put(LocalDate.now().minusDays(1l).atTime(9, 00), 30); // yesterday 9:00 am (before yesterdays trading hours)
    	stockPrices.put(LocalDate.now().minusDays(1l).atTime(23, 38), 10); // yesterday 11:38 pm
    	stockPrices.put(LocalDate.now().atTime(11, 53), 11); // today 11:53 am
    	stockPrices.put(LocalDate.now().atTime(23, 53), 35); // today 11:53 pm
    	
    	assertEquals(6, Genoapay.getMaxProfit(stockPrices));
    }
    
    /**
     * Test Stock prices before yesterdays trade opening hours
     */
    public void testBeforeTradingHours()
    {
    	Map<LocalDateTime, Integer> stockPrices = new HashMap<LocalDateTime, Integer>();
    	stockPrices.put(LocalDate.now().minusDays(1l).atTime(8, 00), 8); // yesterday 11:00 am
    	stockPrices.put(LocalDate.now().minusDays(1l).atTime(7, 00), 5); // yesterday 12:00 pm
    	stockPrices.put(LocalDate.now().minusDays(1l).atTime(9, 00), 30); // yesterday 9:00 am (before yesterdays trading hours)
    	
    	assertEquals(0, Genoapay.getMaxProfit(stockPrices));
    }
    
    /**
     * Test future stock prices
     */
    public void testFutureBuy()
    {
    	Map<LocalDateTime, Integer> stockPrices = new HashMap<LocalDateTime, Integer>();
    	stockPrices.put(LocalDateTime.now().plusMinutes(10), 8); // yesterday 11:00 am
    	stockPrices.put(LocalDateTime.now().plusHours(2), 5); // yesterday 12:00 pm
    	stockPrices.put(LocalDateTime.now().plusMinutes(50), 30); // yesterday 9:00 am (before yesterdays trading hours)
    	
    	assertEquals(0, Genoapay.getMaxProfit(stockPrices));
    }
    
    
}
