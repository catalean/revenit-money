package ro.cb.finance.storage.h2;

import ro.cb.finance.storage.RatesStorage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 */
public final class H2RatesStorage implements RatesStorage {

    /** The name of the JPA persistence unit configured for storage */
    public static final String PERSISTENCE_UNIT_NAME = "cb-finance-pu";

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Constructor..
     */
    protected H2RatesStorage() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    protected EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    public void close() {
        entityManagerFactory.close();
    }
}
