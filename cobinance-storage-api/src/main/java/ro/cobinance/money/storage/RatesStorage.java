package ro.cobinance.money.storage;

import ro.cobinance.money.storage.model.Date;
import ro.cobinance.money.storage.model.Rate;

import java.util.Collection;

/**
 *
 */
public interface RatesStorage {

    /**
     * @return
     */
    boolean isEmpty();

    /**
     * @param rate
     */
    void addRate(Rate rate);

    /**
     * @param rates
     */
    void addRates(Collection<Rate> rates);

    /**
     * @param refCurrencyCode
     * @param currencyCode
     * @return
     */
    Rate getRate(String refCurrencyCode, String currencyCode);

    /**
     * @param refCurrencyCode
     * @param currencyCode
     * @param rateDate
     * @return
     */
    Rate getRate(String refCurrencyCode, String currencyCode, Date rateDate);

    /**
     * @return
     */
    Date getLatestRateDate();

    /**
     * @return
     */
    Date getLatestRateEffectiveDate();

    /**
     *
     */
    void close();
}
