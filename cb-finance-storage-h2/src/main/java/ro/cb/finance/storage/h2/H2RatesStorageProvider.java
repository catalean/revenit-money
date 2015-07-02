package ro.cb.finance.storage.h2;

import ro.cb.finance.storage.RatesStorage;
import ro.cb.finance.storage.RatesStorageProvider;

/**
 *
 */
public final class H2RatesStorageProvider extends RatesStorageProvider {

    @Override
    protected RatesStorage getStorage() {
        return new H2RatesStorage();
    }
}
