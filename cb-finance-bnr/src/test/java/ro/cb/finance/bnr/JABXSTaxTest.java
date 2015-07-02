package ro.cb.finance.bnr;

import org.junit.Test;
import ro.cb.finance.bnr.xml.model.LTCube;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Catalin Kormos
 */
public class JABXSTaxTest {

    @Test
    public void testParse() throws JAXBException, XMLStreamException, IOException {
        // set up a StAX reader
        XMLInputFactory xmlif = XMLInputFactory.newInstance();

        InputStream in = getClass().getResourceAsStream("/nbrfxrates2014.xml");

        URL url = new URL(BNR.LAST_10_RATES);

        URLConnection connection = url.openConnection();
        in = connection.getInputStream();

        XMLStreamReader xmlr = xmlif.createXMLStreamReader(in);

        JAXBContext ucontext = JAXBContext.newInstance(LTCube.class);

        Unmarshaller unmarshaller = ucontext.createUnmarshaller();

        try {
            while(xmlr.hasNext()){
                xmlr.next();
                if (xmlr.getEventType() == XMLStreamReader.START_ELEMENT){
                    if ("Cube".equalsIgnoreCase(xmlr.getLocalName())) {
                        JAXBElement<LTCube> pt = unmarshaller.unmarshal(xmlr, LTCube.class);

                        LTCube cube = pt.getValue();
                        System.out.println(cube.getDate());

                        if (xmlr.getEventType() == XMLStreamConstants.CHARACTERS) {
                            xmlr.next();
                        }
                    }
                }
            }



        } finally {
            xmlr.close();
        }
    }
}
