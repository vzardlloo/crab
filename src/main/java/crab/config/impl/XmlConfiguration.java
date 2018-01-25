package crab.config.impl;


import crab.config.CrabConfiguration;
import crab.kit.AssertKit;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.io.File;

public class XmlConfiguration implements CrabConfiguration {

    private Document document;
    private String filePath;
    private String xmlPath;

    public XmlConfiguration(String filePath) {
        this.filePath = filePath;
        this.xmlPath = this.getClass().getResource(filePath).toString();
        if (xmlPath.substring(5).indexOf(":") > 0) {
            //windo系统中含有:分隔符
            xmlPath = xmlPath.substring(6);
        } else {
            xmlPath = xmlPath.substring(5);
        }
        SAXReader reader = new SAXReader();
        try {
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            this.document = reader.read(new File(xmlPath));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String attrValue(String strXPath) {
        Node n = document.selectSingleNode(strXPath);
        if (n != null) {
            return (n.valueOf("@value"));
        } else {
            return null;
        }
    }


    @Override
    public String getValue(String key) {
        AssertKit.checkNotNull(key);
        String strPath = "//crabConfig/item[@key='" + key + "']";
        String value = attrValue(strPath);
        if (value == null) {
            throw new RuntimeException("this value not exist! maybe you can check the key is right or not! ");
        }
        return value;
    }

    @Override
    public Integer getIntValue(String key) {
        return Integer.parseInt(getValue(key));
    }

    @Override
    public Long getLongValue(String key) {
        return Long.parseLong(getValue(key));
    }

    @Override
    public Double getDoubleValue(String key) {
        return Double.parseDouble(getValue(key));
    }
}
