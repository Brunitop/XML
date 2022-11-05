package XMLDOM;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class AreaCalc {
    static final String CLASS_NAME = AreaCalc.class.getSimpleName();
    static final Logger LOG = Logger.getLogger(CLASS_NAME);
    static final double pi = 3.14159;

    public static void main(String argv[]) {
        if (argv.length != 1) {
            LOG.severe("Falta archivo SVG como argumento.");
            System.exit(1);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            //dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            //dbf.setFeature(XMLConstants.ACCESS_EXTERNAL_DTD, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(argv[0]));
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();

            LOG.info("Root: " + root.getNodeName());

            NodeList nList = root.getChildNodes();
            LOG.info("Child nodes: " + nList.getLength());
            handleElement(nList);

        } catch (ParserConfigurationException e) {
            LOG.severe(e.getMessage());
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        } catch (SAXException e) {
            LOG.severe(e.getMessage());
        }
    }

    private static void handleElement(NodeList nList) {
        int x1 = 0, x2 = 0; //posición x de la recta
        int y1 = 0, y2 = 0; //posición y de la recta
        int height = 0; //alto
        int width = 0; //ancho
        double dist = 0; //distancia entre puntos a y b de la recta
        int r = 0; //radio
        double area = 0, perimetro = 0;
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Node Name = " + node.getNodeName() //Nombre del nodo
                        + "; Value = " + node.getTextContent());
                if (node.hasAttributes()) {
                    // obtener nombres y valores de atributos
                    NamedNodeMap nodeMap = node.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node tempNode = nodeMap.item(i);
                        System.out.println("Nombre de atributo : " + tempNode.getNodeName() //atributo
                                + "; Value = " + tempNode.getNodeValue());
                        if (node.getNodeName() == "rect") { //guardar atributos de rectangulo
                            if (tempNode.getNodeName() == "height") //Guardar altura
                                height = Integer.parseInt(tempNode.getNodeValue());
                            if (tempNode.getNodeName() == "width") //guardar ancho
                                width = Integer.parseInt(tempNode.getNodeValue());
                        } else if (node.getNodeName() == "circle") { //guardar atributos de circulo
                            if (tempNode.getNodeName() == "r") //Guardar radio
                                r = Integer.parseInt(tempNode.getNodeValue());
                        } else if(node.getNodeName() == "line"){ //guardar atributo de linea
                            if (tempNode.getNodeName() == "x1") //guardar x1
                                x1 = Integer.parseInt(tempNode.getNodeValue());
                            if (tempNode.getNodeName() == "y1") //guardar y1
                                y1 = Integer.parseInt(tempNode.getNodeValue());
                            if (tempNode.getNodeName() == "x2") //guardar x2
                                x2 = Integer.parseInt(tempNode.getNodeValue());
                            if (tempNode.getNodeName() == "y2") //guardar y2
                                y2 = Integer.parseInt(tempNode.getNodeValue());
                        }
                    }
                    if (node.hasChildNodes()) {
                        // Tenemos más hijos; Visitémoslos también.
                        handleElement(node.getChildNodes());

                    }
                }
                if (node.getNodeName() == "rect") { //calcular valores recta
                    perimetro = (width*2) + (height*2);
                    System.out.println("Perimetro de rectangulo: " + perimetro);
                    area = width * height;
                    System.out.println("Area de rectangulo: " + area);
                } else if (node.getNodeName() == "circle") {
                    perimetro = 2*pi*r;
                    System.out.println("Perimetro de rectangulo: " + perimetro);
                    area = pi * Math.pow(r,2);
                    System.out.println("Area de rectangulo: " + area);
                } else if(node.getNodeName() == "line"){
                    dist = Math.sqrt(Math.pow((x1+x2),2)+Math.pow((y1+y2),2));
                    System.out.println("Longitud de linea:" + dist);
                }
            }
        }
    }
}