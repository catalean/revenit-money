package ro.codebeat.finance.storage.h2;

import ro.codebeat.finance.storage.RatesStorage;
import ro.codebeat.finance.storage.model.Bank;
import ro.codebeat.finance.storage.model.Currency;
import ro.codebeat.finance.storage.model.Date;
import ro.codebeat.finance.storage.model.Rate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class H2RatesStorage implements RatesStorage {

    /** The name of the JPA persistence unit configured for storage */
    public static final String PERSISTENCE_UNIT_NAME = "cb-finance-pu";

    private EntityManagerFactory emf;

    /**
     * Constructor..
     */
    protected H2RatesStorage() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @Override
    public boolean isEmpty() {
        EntityManager em = emf.createEntityManager();

        try {

            Long ratesCount = (Long)em.createQuery("select count(r.id) from Rate r").getSingleResult();
            return ratesCount == 0;

        } finally {
            em.close();
        }
    }

    @Override
    public void addRate(Rate rate) {
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        try {
            addRate(rate, em);
            em.getTransaction().commit();
        } catch (Exception exc) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void addRates(Collection<Rate> rates) {
        EntityManager em = emf.createEntityManager();

        Map<String, Bank> banksByName = new HashMap<>();
        Map<String, Currency> currenciesByCode = new HashMap<>();
        Map<String, Date> datesByKey = new HashMap<>();

        em.getTransaction().begin();
        try {
            for (Rate aRate : rates) {
                addRate(aRate, em, banksByName, currenciesByCode, datesByKey);
            }
            em.getTransaction().commit();
        } catch (Exception exc) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Rate getRate(String refCurrencyCode, String currencyCode) {
        return getRate(refCurrencyCode, currencyCode, Date.today());
    }

    @Override
    public Rate getRate(String refCurrencyCode, String currencyCode, Date rateDate) {
        EntityManager em = emf.createEntityManager();

        try {
            Query query = em.createQuery("select r from Rate r " +
                    "where r.referenceCurrency.code = :refCode and r.currency.code = :code and " +
                    "r.date.dayOfMonth = :dayOfMonth and r.date.monthOfYear = :monthOfYear and r.date.year = :year");
            query.setParameter("refCode", refCurrencyCode);
            query.setParameter("code", currencyCode);
            query.setParameter("dayOfMonth", rateDate.getDayOfMonth());
            query.setParameter("monthOfYear", rateDate.getMonthOfYear());
            query.setParameter("year", rateDate.getYear());

            return toSingleResult(query);
        } finally {
            em.close();
        }
    }

    /**
     * @param rate
     * @param em
     */
    private void addRate(Rate rate, EntityManager em) {
        this.addRate(rate, em, new HashMap<String, Bank>(), new HashMap<String, Currency>(), new HashMap<String, Date>());
    }

    /**
     * @param rate
     * @param em
     * @param banksByName
     * @param currenciesByCode
     * @param datesByKey
     */
    private Rate addRate(Rate rate, EntityManager em, Map<String, Bank> banksByName, Map<String, Currency> currenciesByCode, Map<String, Date> datesByKey) {
        //make sure all dimensions are existing before hand

        //make sure the bank is a persistent one
        Bank bank = banksByName.get(rate.getBank().getName());
        if (bank == null) {
            bank = ensureBankPersistent(rate.getBank(), em);
            banksByName.put(rate.getBank().getName(), bank);
        }
        rate = rate.withBank(bank);

        //make sure the reference currency is a persistent one
        Currency referenceCurrency = currenciesByCode.get(rate.getReferenceCurrency().getCode());
        if (referenceCurrency == null) {
            referenceCurrency = ensureCurrencyPersistent(rate.getReferenceCurrency(), em);
            currenciesByCode.put(referenceCurrency.getCode(), referenceCurrency);
        }
        rate = rate.withReferenceCurrency(referenceCurrency);

        //make sure the date is a persistent one
        Date date = datesByKey.get(rate.getDate().getYear() + "" + rate.getDate().getMonthOfYear() + "" + rate.getDate().getDayOfMonth());
        if (date == null) {
            date = ensureDatePersistent(rate.getDate(), em);
            datesByKey.put(date.getYear() + "" + date.getMonthOfYear() + "" + date.getDayOfMonth(), date);
        }
        rate = rate.withDate(date);

        //make sure the effective date is a persistent one
        Date effectiveDate = datesByKey.get(rate.getEffectiveDate().getYear() + "" + rate.getEffectiveDate().getMonthOfYear() + "" + rate.getEffectiveDate().getDayOfMonth());
        if (effectiveDate == null) {
            effectiveDate = ensureDatePersistent(rate.getEffectiveDate(), em);
            datesByKey.put(effectiveDate.getYear() + "" + effectiveDate.getMonthOfYear() + "" + effectiveDate.getDayOfMonth(), effectiveDate);
        }
        rate = rate.withEffectiveDate(effectiveDate);

        //make sure the currency is a persistent one
        Currency currency = currenciesByCode.get(rate.getCurrency().getCode());
        if (currency == null) {
            currency = ensureCurrencyPersistent(rate.getCurrency(), em);
            currenciesByCode.put(currency.getCode(), currency);
        }
        rate = rate.withCurrency(currency);

        em.persist(rate);

        return rate;
    }

    /**
     * @param bank
     * @param em
     * @return
     */
    private Bank ensureBankPersistent(Bank bank, EntityManager em) {
        Bank persistentBank = toSingleResult(em.createQuery("select b from Bank b where b.name = :bankName").setParameter("bankName", bank.getName()));

        if (persistentBank == null) {
            em.persist(bank);
            return bank;
        } else {
            return persistentBank;
        }
    }

    /**
     * @param date
     * @param em
     * @return
     */
    private Date ensureDatePersistent(Date date, EntityManager em) {
        Date persistentDate = toSingleResult(em.createQuery("select d from Date d where d.dayOfMonth = :dayOfMonth and d.monthOfYear = :monthOfYear and d.year = :year")
                .setParameter("dayOfMonth", date.getDayOfMonth()).setParameter("monthOfYear", date.getMonthOfYear()).setParameter("year", date.getYear()));

        if (persistentDate == null) {
            em.persist(date);
            return date;
        } else {
            return persistentDate;
        }
    }

    /**
     * @param currency
     * @param em
     * @return
     */
    private Currency ensureCurrencyPersistent(Currency currency, EntityManager em) {
        Currency persistentCurrency = toSingleResult(em.createQuery("select c from Currency c where c.code = :currCode").setParameter("currCode", currency.getCode()));

        if (persistentCurrency == null) {
            em.persist(currency);
            return currency;
        } else {
            return persistentCurrency;
        }
    }

    @Override
    public Date getLatestRateDate() {
        return null;
    }

    @Override
    public Date getLatestRateEffectiveDate() {
        return null;
    }

    @Override
    public void close() {
        emf.close();
        emf = null;
    }

    /**
     * @param query
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <E> E toSingleResult(Query query) {
        return (E) toSingleResult(query.getResultList());
    }

    /**
     * @param resultList
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <E> E toSingleResult(List resultList) {
        if (resultList.isEmpty()) {
            return null;
        } else if (resultList.size() == 1) {
            return (E) resultList.get(0);
        } else {
            throw new NonUniqueResultException();
        }
    }
}
