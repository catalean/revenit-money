package ro.cb.finance.bnr;

import ro.cb.finance.storage.RatesStorage;
import ro.cb.finance.storage.RatesStorageProvider;

import java.math.BigDecimal;

/**
 * Cursul valutar BNR se actualizeaza in fiecare zi bancara, dupa ora 13:15, cel tarziu pana la ora 14:00. Cursul stabilit de BNR este valabil pentru ziua urmatoare.
 */
public final class BNR {

    /** */
    public static final String XML_SCHEMA       = "http://bnr.ro/xsd/nbrfxrates.xsd";
    /** */
    public static final String LATEST_RATES     = "http://bnr.ro/nbrfxrates.xml";
    /** */
    public static final String LAST_10_RATES    = "http://bnr.ro/nbrfxrates10days.xml";
    /** */
    public static final String ANNUAL_RATES     = "http://bnr.ro/files/xml/years/nbrfxrates%s.xml";

    /**
     * Contacts the website of BNR and downloads the rates that are not yet in the current storage.
     */
    public static void actualizeRates() {
        RatesStorage storage = RatesStorageProvider.open();
        System.out.println(storage);

        if (storage != null) {
            storage.close();
        }
    }

    public BigDecimal convert(BigDecimal amount, String from, String to) {
        return null;
    }

    public static void main(String[] args) {
        BNR.actualizeRates();
    }
}
