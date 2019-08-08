package Support;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import Environments.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


public class ReadXMLUtils {

    public String[] getResultReport() {
        String[] r = null;
        try {
            File fXmlFile = new File(Constants.HTML_XML_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("testsuite");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                r = new String[3];
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    r[0] = eElement.getAttribute("tests");
                    int foo = Integer.parseInt(r[0]);
                    r[1]  = eElement.getAttribute("failures");
                    int foo1 = Integer.parseInt(r[1]);
                    foo = foo - foo1;
                    r[2] = Integer.toString(foo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
}
