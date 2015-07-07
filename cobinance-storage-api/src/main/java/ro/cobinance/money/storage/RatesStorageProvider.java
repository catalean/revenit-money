package ro.cobinance.money.storage;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 *
 */
public abstract class RatesStorageProvider {

    /**
     * @return
     */
    public static RatesStorage open() {
        Iterator<RatesStorageProvider> providers = ServiceLoader.load(RatesStorageProvider.class, getClassLoader()).iterator();
        if (providers.hasNext()) {
            return providers.next().getStorage();
        }

        return null;
    }


    /**
     * @return
     */
    protected abstract RatesStorage getStorage();

    /**
     * Creates a privileged {@link ClassLoader}
     *
     * @return Creates a privileged class-loader
     */
    private static ClassLoader getClassLoader() {
        ClassLoader loader = AccessController.doPrivileged(
                new PrivilegedAction<ClassLoader>() {
                   public ClassLoader run() {
                       try {
                           return Thread.currentThread().getContextClassLoader();
                       } catch (SecurityException e) {
                           return null;
                       }
                   }
               }
        );

        if (loader == null) {
            loader = RatesStorageProvider.class.getClassLoader();
        }

        return loader;
    }
}
