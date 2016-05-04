package com.qa.framework.library.android.uiautomator.tree;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UiHierarchyXmlLoader {
    private BasicTreeNode mRootNode;
    private List<Rectangle> mNafNodes;
    private List<BasicTreeNode> mNodeList;

    public BasicTreeNode parseXml(String xmlPath) {
        this.mRootNode = null;
        this.mNafNodes = new ArrayList();
        this.mNodeList = new ArrayList();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        }
        DefaultHandler handler = new DefaultHandler() {
            BasicTreeNode mParentNode;
            BasicTreeNode mWorkingNode;

            public void startElement(String uri, String localName, String qName, Attributes attributes)
                    throws SAXException {
                boolean nodeCreated = false;

                this.mParentNode = this.mWorkingNode;
                if ("hierarchy".equals(qName)) {
                    int rotation = 0;
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if ("rotation".equals(attributes.getQName(i))) {
                            try {
                                rotation = Integer.parseInt(attributes.getValue(i));
                            } catch (NumberFormatException nfe) {
                            }
                        }
                    }
                    this.mWorkingNode = new RootWindowNode(attributes.getValue("windowName"), rotation);
                    nodeCreated = true;
                } else if ("node".equals(qName)) {
                    UiNode tmpNode = new UiNode();
                    for (int i = 0; i < attributes.getLength(); i++) {
                        tmpNode.addAtrribute(attributes.getQName(i), attributes.getValue(i));
                    }
                    this.mWorkingNode = tmpNode;
                    nodeCreated = true;

                    String naf = tmpNode.getAttribute("NAF");
                    if ("true".equals(naf)) {
                        UiHierarchyXmlLoader.this.mNafNodes.add(new Rectangle(tmpNode.x, tmpNode.y, tmpNode.width, tmpNode.height));
                    }
                }
                if (nodeCreated) {
                    if (UiHierarchyXmlLoader.this.mRootNode == null) {
                        UiHierarchyXmlLoader.this.mRootNode = this.mWorkingNode;
                    }
                    if (this.mParentNode != null) {
                        this.mParentNode.addChild(this.mWorkingNode);
                        UiHierarchyXmlLoader.this.mNodeList.add(this.mWorkingNode);
                    }
                }
            }

            public void endElement(String uri, String localName, String qName)
                    throws SAXException {
                if (this.mParentNode != null) {
                    this.mWorkingNode = this.mParentNode;
                    this.mParentNode = this.mParentNode.getParent();
                }
            }
        };
        try {
            parser.parse(new File(xmlPath), handler);
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return this.mRootNode;
    }

    public List<Rectangle> getNafNodes() {
        return Collections.unmodifiableList(this.mNafNodes);
    }

    public List<BasicTreeNode> getAllNodes() {
        return this.mNodeList;
    }
}
