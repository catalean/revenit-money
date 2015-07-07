package ro.cobinance.money.storage.h2;

import ro.cobinance.money.storage.RatesStorage;
import ro.cobinance.money.storage.RatesStorageProvider;

/**
 *
 */
public final class H2RatesStorageProvider extends RatesStorageProvider {

    @Override
    protected RatesStorage getStorage() {
        return new H2RatesStorage();
    }
}
