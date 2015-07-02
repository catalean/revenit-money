package ro.cb.finance.storage.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper for resolving services via the {@link java.util.ServiceLoader}
 */
public final class SpiHelper {

    protected static final transient List<Class> serviceFilter = new CopyOnWriteArrayList<Class>();
    protected static final transient Map<Class, Object> cache = new ConcurrentHashMap<Class, Object>();
    protected static final Logger logger = Logger.getLogger(SpiHelper.class.getName());

    /**
     * Constructor, private as this class declares only static methods and it should never be instantiated.
     */
    private SpiHelper() {
        //do nothing
    }

    /**
     * Resolves the service for the given class (interface or abstract class).
     *
     * @param targetType current class
     * @param <T>        current type
     * @return found service
     */
    public static <T> T getService(Class<T> targetType) {
        T service = (T) cache.get(targetType);

        if (service != null) {
            return service;
        }

        ServiceLoader<T> providers = ServiceLoader.load(targetType, getClassLoader());
        for (T provider : providers) {
            if (!serviceFilter.contains(targetType)) {
                serviceFilter.add(targetType);
                cache.put(targetType, provider);
                if (logger.isLoggable(Level.INFO)) {
                    logger.info(provider.getClass().getName() + " will be used as " + targetType.getName());
                }
            } else {
                logger.severe(provider.getClass().getName() + " won't be used as " + targetType.getName());
            }
        }
        return (T) cache.get(targetType);
    }

    /**
     * Creates a privileged {@link ClassLoader}
     *
     * @return Creates a privileged class-loader
     */
    private static ClassLoader getClassLoader() {
        ClassLoader loader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
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
            loader = SpiHelper.class.getClassLoader();
        }

        return loader;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
