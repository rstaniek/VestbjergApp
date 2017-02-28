package com.teamSuperior.core.controlLayer;

import com.teamSuperior.core.enums.Currency;
import org.junit.Test;

import static com.teamSuperior.core.controlLayer.WebsiteCrawler.getExchangeRatioBloomberg;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajmu on 17.02.28.
 */
public class WebsiteCrawlerTest {
    @Test
    public void getExchangeRatioBloombergNotNull() throws Exception {
        assertNotNull(getExchangeRatioBloomberg(Currency.DKKEUR));
        assertNotNull(getExchangeRatioBloomberg(Currency.DKKUSD));
        assertNotNull(getExchangeRatioBloomberg(Currency.EURDKK));
        assertNotNull(getExchangeRatioBloomberg(Currency.USDDKK));
    }

}