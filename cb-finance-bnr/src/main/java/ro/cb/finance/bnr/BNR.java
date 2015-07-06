package ro.cb.finance.bnr;

import ro.cb.finance.bnr.xml.model.LTCube;
import ro.cb.finance.storage.RatesStorage;
import ro.cb.finance.storage.RatesStorageProvider;
import ro.cb.finance.storage.model.Bank;
import ro.cb.finance.storage.model.Currency;
import ro.cb.finance.storage.model.Date;
import ro.cb.finance.storage.model.Rate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Cursul valutar BNR se actualizeaza in fiecare zi bancara, dupa ora 13:15, cel tarziu pana la ora 14:00. Cursul stabilit de BNR este valabil pentru ziua urmatoare.
 */
public final class BNR {

    /** */
    public static final String XML_SCHEMA_FILE_NAME = "nbrfxrates.xsd";
    /** */
    public static final String XML_SCHEMA_URL       = "http://bnr.ro/xsd/" + XML_SCHEMA_FILE_NAME;

    /** */
    public static final String LATEST_RATES_FILE_NAME = "nbrfxrates.xml";
    /** */
    public static final String LATEST_RATES_URL       = "http://bnr.ro/" + LATEST_RATES_FILE_NAME;

    /** */
    public static final String LAST_10_RATES_FILE_NAME = "nbrfxrates10days.xml";
    /** */
    public static final String LAST_10_RATES_URL       = "http://bnr.ro/" + LAST_10_RATES_FILE_NAME;

    /** */
    public static final String ANNUAL_RATES_FILE_NAME = "nbrfxrates%s.xml";
    /** */
    public static final String ANNUAL_RATES_URL       = "http://bnr.ro/files/xml/years/" + ANNUAL_RATES_FILE_NAME;

    /**
     * Contacts the website of BNR and downloads the rates that are not yet in the current storage.
     */
    public static void actualizeRates() throws IOException, JAXBException, XMLStreamException {
        RatesStorage storage = RatesStorageProvider.open();
        if (storage == null) {
            return;
        }

        LocalDate today = LocalDate.now();

        if (!storage.isEmpty()) {
            Date latestRateDate = storage.getLatestRateDate();
            System.out.println("latestRateDate = " + latestRateDate);
        } else {
            Collection<File> sourceFiles = new ArrayList<>();

            //download everything and fill up the database - BNR offers the rates starting from 2005 to today
            for (int i = 2005; i <= today.getYear(); i++) {
                String targetFileName = String.format(ANNUAL_RATES_FILE_NAME, String.valueOf(i));
                String sourceURL = String.format(ANNUAL_RATES_URL, String.valueOf(i));

                File targetDir = new File(System.getProperty("user.home"), ".cb-finance");

                //check if the file exists already locally - to minimize what we need to download
                File targetFile = new File(targetDir, targetFileName);
                if (!targetFile.exists()) {
                    targetFile = downloadFile(new URL(sourceURL), targetDir, targetFileName);
                }

                sourceFiles.add(targetFile);
            }

            createStorage(sourceFiles, storage);
        }

        storage.close();
    }

    /**
     * @param sourceFiles
     * @param targetStorage
     */
    private static void createStorage(Collection<File> sourceFiles, RatesStorage targetStorage) throws JAXBException, FileNotFoundException, XMLStreamException {
        if (sourceFiles == null || sourceFiles.isEmpty()) {
            return;
        }

        XMLInputFactory xmlif = XMLInputFactory.newInstance();

        JAXBContext context = JAXBContext.newInstance(LTCube.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        for (File aFile : sourceFiles) {
            FileInputStream in = new FileInputStream(aFile);
            XMLStreamReader xmlr = xmlif.createXMLStreamReader(in);

            try {
                while(xmlr.hasNext()){
                    xmlr.next();
                    if (xmlr.getEventType() == XMLStreamReader.START_ELEMENT){
                        if ("Cube".equalsIgnoreCase(xmlr.getLocalName())) {
                            JAXBElement<LTCube> pt = unmarshaller.unmarshal(xmlr, LTCube.class);

                            LTCube cube = pt.getValue();

                            Date date = new Date(cube.getDate().getDay(), cube.getDate().getMonth(), cube.getDate().getYear());
                            Date effectiveDate = date.tomorrow();

                            Collection<Rate> rates = new ArrayList<Rate>();
                            for (LTCube.Rate inRate : cube.getRate()) {
                                int multiplier = inRate.getMultiplier() != null ? inRate.getMultiplier().intValue() : 1;

                                if (inRate.getValue() != null) {
                                    Rate rate = new Rate(new Bank("BNR"), new Currency("RON"), date, effectiveDate,
                                            new Currency(inRate.getCurrency()), inRate.getValue().movePointRight(4).intValueExact(), 4, multiplier);
                                    rates.add(rate);
                                }
                            }

                            targetStorage.addRates(rates);
                        }
                    }
                }



            } finally {
                xmlr.close();
            }
        }
    }

    /**
     * @param amount
     * @param fromCurrencyCode
     * @param toCurrencyCode
     * @return
     */
    public static BigDecimal convert(int amount, String fromCurrencyCode, String toCurrencyCode) {
        RatesStorage storage = RatesStorageProvider.open();

        try {
            Rate rate = storage.getRate("RON", "EUR");

            if (rate == null) {
                System.out.println("No rate available");
                return null;
            }

            System.out.println("reference rate = " + rate.getReferenceCurrency().getCode());

            long amountAsCents = amount;
            long resultAsCents = rate.getReferenceCurrency().getCode().equals(fromCurrencyCode) ? amountAsCents / rate.getValue() : amountAsCents * rate.getValue();

            System.out.println("amountAsCents = " + amountAsCents);
            System.out.println("resultAsCents = " + resultAsCents);

            System.out.println(amount + " " + fromCurrencyCode + " to " + toCurrencyCode + " at rate = "+ BigDecimal.valueOf(rate.getValue()).scaleByPowerOfTen(-4) + " = " + BigDecimal.valueOf(resultAsCents).scaleByPowerOfTen(-4));

            return null;
        } finally {
            storage.close();
        }
    }

    /**
     * @param targetUrl
     * @param targetDir
     * @param targetFileName
     * @return
     * @throws IOException
     */
    private static File downloadFile(URL targetUrl, File targetDir, String targetFileName) throws IOException {
        int BUFFER_SIZE = 4096;

        HttpURLConnection httpConn = (HttpURLConnection) targetUrl.openConnection();
        int responseCode = httpConn.getResponseCode();

        File targetFile = new File(targetDir, targetFileName);

        try {
            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(targetFile);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                return null;
            }
        } finally {
            httpConn.disconnect();
        }

        return targetFile;
    }

    public static void main(String[] args) {
//        try {
//            BNR.actualizeRates();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        }

        BNR.convert(100, "RON", "EUR");
    }
}
