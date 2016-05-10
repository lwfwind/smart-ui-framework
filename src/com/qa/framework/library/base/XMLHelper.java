package com.qa.framework.library.base;

import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * General convenience methods for working with XML
 */
public class XMLHelper {

    private final static Logger logger = Logger
            .getLogger(XMLHelper.class);

    private Document document;

    /**
     * Gets document.
     *
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Sets document.
     *
     * @param document the document
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Str to xml xml helper.
     *
     * @param str the str
     * @return the xml helper
     */
    public XMLHelper strToXML(String str) {
        try {
            setDocument(DocumentHelper.parseText(str));
        } catch (DocumentException e) {
            logger.error(e.toString());
        }
        return this;
    }

    /**
     * Create the instance of the Document class
     */
    public void createDocument() {
        setDocument(DocumentHelper.createDocument());
    }

    /**
     * Create the Processing Instruction
     *
     * @param strProcessingInstructionName  the str processing instruction name
     * @param strProcessingInstructionValue the str processing instruction value
     * @throws DocumentException the document exception
     */
    public void createProcessingInstruction(
            String strProcessingInstructionName,
            String strProcessingInstructionValue) throws DocumentException {
        setDocument(document.addProcessingInstruction(
                strProcessingInstructionName, strProcessingInstructionValue));
    }

    /**
     * Create a root element for object document
     *
     * @param strRootName the root element name to be created
     * @return Element element
     */
    public Element createDocumentRoot(String strRootName) {
        return getDocument().addElement(strRootName);
    }

    /**
     * Add a element under specified parent element
     *
     * @param ParentElement  specified parent element
     * @param strElementNmae the element name to be added
     * @return Element element
     */
    public Element addChildElement(Element ParentElement, String strElementNmae) {
        return ParentElement.addElement(strElementNmae);
    }

    /**
     * Set the element text for the specified element
     *
     * @param Element the element
     * @param strText the text to be set
     */
    public void setText(Element Element, String strText) {
        Element.setText(strText);
    }

    /**
     * Add the attribute name and value under specified element
     *
     * @param Element        the element
     * @param AttributeName  the attribute name
     * @param AttributeValue the attribute value
     */
    public void addAttribute(Element Element, String AttributeName,
                             String AttributeValue) {
        Element.addAttribute(AttributeName, AttributeValue);
    }

    /**
     * Add the specified comment under specified element
     *
     * @param Element    the element
     * @param strComment the str comment
     */
    public void addComment(Element Element, String strComment) {
        Element.addComment(strComment);
    }

    /**
     * Remove the specified child Element under Parent element
     *
     * @param ParentElement  the parent element
     * @param strElementNmae the element name to be removed
     */
    public void removeChildElement(Element ParentElement, String strElementNmae) {
        ParentElement.remove(getChildElement(ParentElement, strElementNmae));
    }

    /**
     * Remove the specified Attribute under specified element
     *
     * @param Element          the element
     * @param strAttributeNmae the attribute name to be removed
     */
    public void removeAttribute(Element Element, String strAttributeNmae) {
        Element.remove(getAttribute(Element, strAttributeNmae));
    }

    /**
     * Save document to specified XML file
     *
     * @param strFileName the XML file name to be saved
     */
    public void saveTo(String strFileName) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            XMLWriter writer = new XMLWriter(new FileOutputStream(strFileName), format);
            writer.write(getDocument());
            writer.close();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Read and parse XML file
     *
     * @param sFileName The full path of the XML file
     * @return Document document
     */
    public Document readXMLFile(String sFileName) {
        SAXReader reader = new SAXReader();
        setDocument(null);
        try {
            setDocument(reader.read(new File(sFileName)));
        } catch (DocumentException e) {
            logger.error(e.toString());
        }
        return getDocument();
    }

    /**
     * Get the root element
     *
     * @return Element root element
     */
    public Element getRootElement() {
        return getDocument().getRootElement();
    }

    /**
     * Get the specified child element
     *
     * @param ParentElement  the parent element
     * @param strElementNmae the str element nmae
     * @return Element child element
     */
    public Element getChildElement(Element ParentElement, String strElementNmae) {
        return ParentElement.element(strElementNmae);
    }

    /**
     * Get the specified child element
     *
     * @param ParentElement  the parent element
     * @param strElementNmae the str element nmae
     * @return Element child elements
     */
    @SuppressWarnings("unchecked")
    public List<Element> getChildElements(Element ParentElement,
                                          String strElementNmae) {
        return ParentElement.elements(strElementNmae);
    }

    /**
     * Get the text of the specified element
     *
     * @param element the element
     * @return String text
     */
    public String getText(Element element) {
        return element.getText();
    }

    /**
     * Get the text of the specified child element
     *
     * @param ParentElement  the parent element
     * @param strElementNmae the str element nmae
     * @return child text
     */
    public String getChildText(Element ParentElement, String strElementNmae) {
        return getText(getChildElement(ParentElement, strElementNmae));
    }

    /**
     * Get the attribute of the specified element
     *
     * @param element          the element
     * @param strAttributeNmae the str attribute nmae
     * @return attribute attribute
     */
    public Attribute getAttribute(Element element, String strAttributeNmae) {
        return element.attribute(strAttributeNmae);
    }

    /**
     * Get the attribute text of the specified element
     *
     * @param element          the element
     * @param strAttributeNmae the str attribute nmae
     * @return attribute text
     */
    public String getAttributeText(Element element, String strAttributeNmae) {
        return element.attribute(strAttributeNmae).getText();
    }

    /**
     * Set the attribute text of the specified element
     *
     * @param element           the element
     * @param strAttributeNmae  the str attribute nmae
     * @param strAttributeValue the str attribute value
     */
    public void setAttributeText(Element element, String strAttributeNmae,
                                 String strAttributeValue) {
        element.attribute(strAttributeNmae).setText(strAttributeValue);
    }

    /**
     * Get element by XPath
     *
     * @param xpath such as /company/department[name = 'HR']/employee[1]
     * @return Element element
     */
    public Element findElementByXPath(String xpath) {
        return (Element) getDocument().selectSingleNode(xpath);
    }

    /**
     * Find elements by x path list.
     *
     * @param xpath the xpath
     * @return the list
     */
    public List<Element> findElementsByXPath(String xpath) {
        @SuppressWarnings("unchecked")
        List<Element> elements = (List<Element>) getDocument().selectNodes(
                xpath);
        return (List<Element>) elements;
    }

    /**
     * Get attribute by XPath
     *
     * @param xpath such as /company/department[name = 'HR']/employee[1]/@salary
     * @return Attribute attribute
     */
    public Attribute findAttributeByXPath(String xpath) {
        return (Attribute) getDocument().selectSingleNode(xpath);
    }

    /**
     * Iterate all elements under specified parent element and put the element
     * name and element text into HashMap
     *
     * @param xpath the xpath
     * @param hm    the hm
     */
    public void iterateElements(String xpath, HashMap<String, String> hm) {
        int num = -1;
        List<Element> elements = findElementsByXPath(xpath);
        for (Element element : elements) {
            num = num + 1;
            @SuppressWarnings("unchecked")
            Iterator<Element> iter = element.elementIterator();
            while (iter.hasNext()) {
                Element el = iter.next();
                hm.put(el.getName() + num, el.getText());
            }
        }
    }

    /**
     * Transform XML to HTML
     *
     * @param strInXMLFileName   the str in xml file name
     * @param strXSLFileName     the str xsl file name
     * @param strOutHTMLFileName the str out html file name
     */
    public void applyXSL(String strInXMLFileName, String strXSLFileName,
                         String strOutHTMLFileName) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Templates template = factory.newTemplates(new StreamSource(
                    new FileInputStream(strXSLFileName)));
            Transformer xformer = template.newTransformer();
            Source source = new StreamSource(new FileInputStream(
                    strInXMLFileName));
            Result result = new StreamResult(new FileOutputStream(
                    strOutHTMLFileName));
            xformer.transform(source, result);
        } catch (FileNotFoundException | TransformerFactoryConfigurationError | TransformerException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Two documents are considered to be "similar" if they contain the same
     * elements and attributes regardless of order.
     *
     * @param xml1 the xml 1
     * @param xml2 the xml 2
     * @return boolean boolean
     */
    public boolean isSimilar(String xml1, String xml2) {
        Diff diff = null;
        try {
            diff = new Diff(xml1, xml2);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return diff != null && diff.similar();
    }

    /**
     * Two XML documents are considered to be "identical" if they contain the
     * same elements and attributes in the same order.
     *
     * @param xml1 the xml 1
     * @param xml2 the xml 2
     * @return boolean boolean
     */
    public boolean isIdentical(String xml1, String xml2) {
        Diff diff = null;
        try {
            diff = new Diff(xml1, xml2);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return diff != null && diff.identical();
    }

    /**
     * Gets all differences.
     *
     * @param xml1 the xml 1
     * @param xml2 the xml 2
     * @throws SAXException the sax exception
     * @throws IOException  the io exception
     */
    public void getAllDifferences(String xml1, String xml2)
            throws SAXException, IOException {
        DetailedDiff myDiff = new DetailedDiff(new Diff(xml1, xml2));
        @SuppressWarnings("unchecked")
        List<Difference> allDifferences = myDiff.getAllDifferences();
        for (Difference d : allDifferences) {
            logger.error(d.toString());
        }
    }

    /**
     * Get the number of the difference
     *
     * @param xml1 the xml 1
     * @param xml2 the xml 2
     * @return the diff count
     * @throws SAXException the sax exception
     * @throws IOException  the io exception
     */
    public int getDiffCount(String xml1, String xml2) throws SAXException,
            IOException {
        DetailedDiff myDiff = new DetailedDiff(new Diff(xml1, xml2));
        @SuppressWarnings("unchecked")
        List<Difference> allDifferences = myDiff.getAllDifferences();
        return allDifferences.size();
    }

}
