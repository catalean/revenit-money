package ro.codebeat.finance.storage.h2;

import ro.codebeat.finance.storage.RatesStorage;
import ro.codebeat.finance.storage.RatesStorageProvider;

/**
 *
 */
public final class H2RatesStorageProvider extends RatesStorageProvider {

    @Override
    protected RatesStorage getStorage() {
        return new H2RatesStorage();
    }
}
