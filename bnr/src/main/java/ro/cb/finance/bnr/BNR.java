package ro.cb.finance.bnr;

import org.xml.sax.SAXException;
import ro.cb.finance.bnr.xml.model.DataSet;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Catalin Kormos
 */
public final class BNR {

    /** */
    public static final String XML_SCHEMA       = "http://bnr.ro/xsd/nbrfxrates.xsd";
    /** */
    public static final String LATEST_RATES     = "http://bnr.ro/nbrfxrates.xml";
    /** */
    public static final String LAST_10_RATES    = "http://bnr.ro/nbrfxrates10days.xml";
    /** */
    public static final String ANNUAL_RATES      = "http://bnr.ro/files/xml/years/nbrfxrates%s.xml";

    private Unmarshaller unmarshaller;

    public BNR() throws JAXBException, SAXException, IOException {
        JAXBContext jc = JAXBContext.newInstance(DataSet.class);

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(getClass().getResourceAsStream("/nbrfxrates.xsd")));

        unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(schema);
        unmarshaller.setListener(new UnmarshallListener());
        unmarshaller.setAdapter(new XmlAdapter() {
            @Override
            public Object unmarshal(Object v) throws Exception {
                System.out.println("unmarshal "+v);
                return v;
            }

            @Override
            public Object marshal(Object v) throws Exception {
                return null;
            }
        });
    }

    public void init(InputStream source) throws JAXBException {
        DataSet dataSet = (DataSet)unmarshaller.unmarshal(source);


    }

    private static final class UnmarshallListener extends Unmarshaller.Listener {

        @Override
        public void afterUnmarshal(Object target, Object parent) {
            System.out.println("after "+target+", "+parent);
        }

        @Override
        public void beforeUnmarshal(Object target, Object parent) {
            System.out.println("before " + target + ", " + parent);
        }
    }
}
