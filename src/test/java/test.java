import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Andrei_Belousov on 1/25/2017.
 */
public class test {

    @Test
    public void import_data () throws IOException, JAXBException, SAXException {

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File("src/main/resources/data/products/schema.xsd"));

        File productsXML = new File("src/main/resources/data/products/product_sample.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(ProductRecordRoot.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ProductRecordRoot products = (ProductRecordRoot) jaxbUnmarshaller.unmarshal(productsXML);
        Map productMap = products.toMap();

        String urlString = "http://localhost:8983/solr/products/";
        SolrClient solr = new HttpSolrClient.Builder(urlString).build();

        SolrInputDocument document = new SolrInputDocument();
        productMap.forEach((k,v)->{
                    if (v == null)
                        document.addField((String)k, "");
                    else
                        document.addField((String)k, v);
                }
        );
        try {
            UpdateResponse response = solr.add(document);
            solr.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
