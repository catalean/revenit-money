package ro.cb.finance.storage;

import ro.cb.finance.storage.util.SpiHelper;

/**
 *
 */
public abstract class RatesStorageProvider {

    private static RatesStorage INSTANCE = null;

    /**
     * @return
     */
    public static RatesStorage open() {
        if (INSTANCE == null) {
            RatesStorageProvider provider = SpiHelper.getService(RatesStorageProvider.class);
            if (provider == null) {
                return null;
            }

            INSTANCE = provider.getStorage();
        }

        return INSTANCE;
    }


    /**
     * @return
     */
    protected abstract RatesStorage getStorage();
}
