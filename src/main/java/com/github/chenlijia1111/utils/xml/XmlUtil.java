package com.github.chenlijia1111.utils.xml;

import com.github.chenlijia1111.utils.core.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * xml 工具类
 * CDATA 标签 xml转移内容 有些特殊内容需要转译 需要用这个标签包裹 如 > <
 * 用法 <return_code><![CDATA[SUCCESS]]></return_code>
 *
 * @author 陈礼佳
 * @since 2019/9/15 12:43
 */
public class XmlUtil {


    /**
     * 解析xml文件
     * xml文件的头 一般都以 <xml> 开头 所以解析出的map 会排除最外层
     * 只解析节点的名称以及值 不解析属性
     *
     * @param inputStream  如果想用字符串为参数 可以通过 {@link java.io.ByteArrayInputStream} 进行转换
     * @return
     * @see #parseXMLToMap(InputStream) 解析xml文件转为map
     */
    public static Map<String, Object> parseXMLToMap(InputStream inputStream) {

        //获取jaxp工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            //获取解析器
            DocumentBuilder builder = factory.newDocumentBuilder();
            //用解析器加载xml文档--->Document
            Document document = builder.parse(inputStream);
            //根节点元素
            Element documentElement = document.getDocumentElement();
            Map<String, Object> map = recursiveFindXmlElement(documentElement);
            return map;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 递归获取 节点的值 节点可能会有下级
     *
     * @param element
     * @return
     */
    private static Map<String, Object> recursiveFindXmlElement(Element element) {
        if (Objects.nonNull(element)) {
            HashMap<String, Object> map = new HashMap<>();
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                short nodeType = item.getNodeType();
                //只取节点元素
                if (Objects.equals(nodeType, (short) 1)) {
                    String nodeName = item.getNodeName();
                    //判断这个节点有没有子节点,如果有子节点就做递归操作
                    Element childElement = (Element) item;
                    Map<String, Object> childMap = recursiveFindXmlElement(childElement);
                    if (null != childMap && childMap.size() > 0) {
                        //有子节点
                        map.put(nodeName, childMap);
                    } else {
                        //没有子节点
                        String nodeValue = StringUtils.isEmptyTrimWhitespace(item.getTextContent())
                                ? null : item.getTextContent().trim();
                        map.put(nodeName, nodeValue);
                    }
                }
            }
            if (map.size() > 0) {
                return map;
            }
        }
        return null;
    }


    /**
     * 将map 转换为xml
     *
     * @param map
     * @return
     */
    public static String parseMapToXml(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("<xml>");
        if (null != map && map.size() > 0) {
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                sb.append("<" + key + ">");
                if (Objects.nonNull(value)) {
                    sb.append("<![CDATA[" + value.toString() + "]]>");
                }
                sb.append("</" + key + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

}
