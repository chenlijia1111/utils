package com.github.chenlijia1111.utils.xml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
     * @param inputStream 如果想用字符串为参数 可以通过 {@link java.io.ByteArrayInputStream} 进行转换
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
     * 解析xml文件
     * xml文件的头 一般都以 <xml> 开头 所以解析出的map 会排除最外层
     * 只解析节点的名称以及值 不解析属性
     *
     * @param s
     * @return
     * @see #parseXMLToMap(InputStream) 解析xml文件转为map
     */
    public static Map<String, Object> parseXMLToMap(String s) {
        try {
            Map<String, Object> stringObjectMap = parseXMLToMap(new ByteArrayInputStream(s.getBytes(CharSetType.UTF8.getType())));
            return stringObjectMap;
        } catch (UnsupportedEncodingException e) {
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
     * 只能转换一层
     * 不支持多层数据
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


    /**
     * 将json 转换为xml
     * 支持复杂数据
     * <p>
     * 如果是数组 不可以为基本类型的数组 必须是对象
     * 因为基本类型会导致无法确定节点的名称
     * <p>
     * 如果是数组的话,默认节点就叫做 <node></node>
     *
     * @param jsonStr         json字符串数组
     * @param defaultNodeName 默认节点名称 如果是json对象数组的话，就需要一个默认的节点名称了,否则，就用默认的 node
     * @return
     */
    public static String parseJsonToXml(String jsonStr, String defaultNodeName) {

        StringBuilder sb = new StringBuilder("<xml>");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonStr);
            //如果是数组的话，用数组的处理方式处理
            if (jsonNode.isArray()) {
                sb.append(dealJsonNodeWithArray(jsonNode, StringUtils.isNotEmpty(defaultNodeName) ? defaultNodeName : "node"));
            } else if (jsonNode.isObject()) {
                //如果是对象的话，用对象的处理方式处理
                sb.append(dealJsonNodeWithObj(jsonNode, null));
            }
            //其他的数据不处理,因为不是对象，没有属性名，就没有节点名称
        } catch (IOException e) {
            e.printStackTrace();
        }

        sb.append("</xml>");
        return sb.toString();
    }


    /**
     * 处理当 jsonNode为数组的时候
     * xml的最外层不能是数组，因为xml以 <xml></xml> 包裹在最外面
     *
     * @param jsonNode 1
     * @param nodeName 这个节点的名称
     * @return java.lang.String
     * @since 下午 9:07 2019/9/19 0019
     **/
    private static String dealJsonNodeWithArray(JsonNode jsonNode, String nodeName) {

        StringBuilder sb = new StringBuilder();
        Iterator<JsonNode> iterator = jsonNode.iterator();
        while (iterator.hasNext()) {
            JsonNode next = iterator.next();
            if (next.isObject()) {
                //如果是对象的话，用对象的处理方式处理,不是对象就不处理
                String s = dealJsonNodeWithObj(next, nodeName);
                sb.append(s);
            }
        }
        return sb.toString();
    }

    /**
     * 处理当 jsonNode为对象的时候
     *
     * @param jsonNode 1
     * @param nodeName 节点名称
     * @return java.lang.String
     * @since 下午 9:07 2019/9/19 0019
     **/
    private static String dealJsonNodeWithObj(JsonNode jsonNode, String nodeName) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(nodeName)) {
            sb.append("<" + nodeName + ">");
        }
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode childJsonNode = jsonNode.get(fieldName);
            if (childJsonNode.isArray()) {
                //如果是数组，调用数组的处理方式进行处理
                sb.append(dealJsonNodeWithArray(childJsonNode, fieldName));
            } else if (childJsonNode.isObject()) {
                //如果是对象，递归调用自己进行处理
                sb.append(dealJsonNodeWithObj(childJsonNode, fieldName));
            } else {
                //否则，就当成文本处理，生成节点的值
                sb.append("<" + fieldName + ">");
                sb.append("<![CDATA[" + childJsonNode.asText() + "]]>");
                sb.append("</" + fieldName + ">");
            }
        }
        if (StringUtils.isNotEmpty(nodeName)) {
            sb.append("</" + nodeName + ">");
        }
        return sb.toString();
    }

}
