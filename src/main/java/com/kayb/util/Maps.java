package com.kayb.util;

import com.kayb.util.xml.XmlReaders;
import com.kayb.util.xml.XmlWriters;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * xml to map or map to xml, nesting not support
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class Maps {

    /**
     * 转换微信XML为Map(仅支持2级)
     * @param xml xml内容
     * @return Map对象
     */
    public static Map<String, Object> toMap(String xml){
        xml = xml.replaceAll("(\\r|\\n)", "");
        if (StringUtil.isEmpty(xml)){
            return Collections.emptyMap();
        }
        XmlReaders readers = XmlReaders.create(xml);
        return toMap(readers);
    }

    /**
     * 转换微信XML为Map(仅支持2级)
     * @param readers xmlReaders
     * @return Map对象
     */
    public static Map<String, Object> toMap(XmlReaders readers){
        Node root = readers.getNode("xml");
        if (root == null){
            return Collections.emptyMap();
        }
        NodeList children = root.getChildNodes();
        if (children.getLength() == 0){
            return Collections.emptyMap();
        }
        Map<String, Object> data = new HashMap<>(children.getLength());
        Node n;
        for (int i = 0; i<children.getLength(); i++){
            n = children.item(i);
            data.put(n.getNodeName(), n.getTextContent());
        }
        return data;
    }

    /**
     * Map转换为XML
     * @param params Map参数
     * @return XML字符串
     */
    public static String toXml(final Map<String, String> params) {
        XmlWriters writers = XmlWriters.create();
        for (Map.Entry<String, String> param : params.entrySet()){
            if (!StringUtil.isEmpty(param.getValue())){
                writers.element(param.getKey(), param.getValue());
            }
        }
        return writers.build();
    }
}
