package cc.ussu.common.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL;
import static com.fasterxml.jackson.databind.MapperFeature.REQUIRE_SETTERS_FOR_GETTERS;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * json utils
 */
@SuppressWarnings("all")
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * can use static singleton, inject: just make sure to reuse!
     */
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            // .configure(ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
            .configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .configure(REQUIRE_SETTERS_FOR_GETTERS, true)
            .setTimeZone(TimeZone.getDefault())
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private JsonUtils() {
        throw new UnsupportedOperationException("Construct JsonUtils");
    }

    public static ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static JsonNode toJsonNode(Object obj) {
        return objectMapper.valueToTree(obj);
    }

    /**
     * 是否空字符串
     */
    private static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 是否是合法json字符串 对象或数组
     */
    public static boolean isTypeJSON(String str) {
        return isTypeJSONObject(str) || isTypeJSONArray(str);
    }

    /**
     * 是否是json字符串
     */
    public static boolean isTypeJSONObject(String str) {
        try {
            parseObject(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 是否是json数组字符串
     */
    public static boolean isTypeJSONArray(String str) {
        try {
            parseArray(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * json representation of object
     *
     * @param object  object
     * @param feature feature
     * @return object to json string
     */
    public static String toJsonString(Object object, SerializationFeature feature) {
        try {
            ObjectWriter writer = objectMapper.writer(feature);
            return writer.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("object to json exception!", e);
        }
        return null;
    }

    /**
     * 对象转json字符串
     *
     * @param object object
     * @return json string
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Object json deserialization exception.", e);
        }
    }

    /**
     * 对象转json字符串
     *
     * @param object
     * @param feature
     * @return
     */
    public static String toJsonStr(Object object, SerializationFeature feature) {
        return toJsonString(object);
    }

    /**
     * 对象转json字符串
     *
     * @param object
     * @return
     */
    public static String toJsonStr(Object object) {
        return toJsonString(object);
    }

    public static String toJSONString(Object obj) {
        return toJsonString(obj);
    }

    /**
     * 此方法将指定的 Json 反序列化为指定类的对象。如果指定的类是泛型类型，则不适合使用它，
     * 因为由于 Java 的类型擦除功能，它不会具有泛型类型信息。因此，如果所需的类型是泛型类型，则不应使用此方法。
     * 请注意，如果指定对象的任何字段是泛型，则此方法工作正常，只是对象本身不应是泛型类型。
     *
     * @param json  the string from which the object is to be deserialized
     * @param clazz the class of T
     * @param <T>   T
     * @return an object of type T from the string
     * classOfT
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("parse object exception!", e);
        }
        return null;
    }

    /**
     * json字符串转bean
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        return parseObject(json, clazz);
    }

    /**
     * JsonNode转bean
     *
     * @param jsonNode
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(JsonNode jsonNode, Class<T> clazz) {
        try {
            return objectMapper.convertValue(jsonNode, clazz);
        } catch (Exception e) {
            logger.error("parse object exception!", e);
        }
        return null;
    }

    /**
     * deserialize
     *
     * @param src   byte array
     * @param clazz class
     * @param <T>   deserialize type
     * @return deserialize type
     */
    public static <T> T parseObject(byte[] src, Class<T> clazz) {
        if (src == null) {
            return null;
        }
        String json = new String(src, UTF_8);
        return parseObject(json, clazz);
    }

    /**
     * json字符串转对象集合
     *
     * @param json  json string
     * @param clazz class
     * @param <T>   T
     * @return list
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (isEmpty(json)) {
            return Collections.emptyList();
        }
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(json, listType);
        } catch (Exception e) {
            logger.error("parse list exception!", e);
        }
        return Collections.emptyList();
    }

    /**
     * check json object valid
     *
     * @param json json
     * @return true if valid
     */
    public static boolean checkJsonValid(String json) {
        if (isEmpty(json)) {
            return false;
        }
        try {
            objectMapper.readTree(json);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * method for finding a json object field with specified name in this
     * node or its child nodes, and returning value it has.
     * if no matching field is found in this node or its descendants, returns null.
     *
     * @param jsonNode  json node
     * @param fieldName Name of field to look for
     * @return Value of first matching node found, if any; null if none
     */
    public static String findValue(JsonNode jsonNode, String fieldName) {
        JsonNode node = jsonNode.findValue(fieldName);
        if (node == null) {
            return null;
        }
        return node.asText();
    }

    /**
     * json字符串转map
     *
     * @param json json
     * @return json to map
     */
    public static Map<String, String> toMap(String json) {
        return parseObject(json, new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * json字符串转map
     *
     * @param json   json
     * @param classK classK
     * @param classV classV
     * @param <K>    K
     * @param <V>    V
     * @return to map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> classK, Class<V> classV) {
        if (isEmpty(json)) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<K, V>>() {
            });
        } catch (Exception e) {
            logger.error("json to map exception!", e);
        }
        return Collections.emptyMap();
    }

    /**
     * json转map
     *
     * @param jsonNode
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toMap(JsonNode jsonNode) {
        if (jsonNode == null) {
            return new HashMap<>();
        }
        try {
            return objectMapper.convertValue(jsonNode, new TypeReference<Map<K, V>>() {
            });
        } catch (Exception e) {
            logger.error("json to map exception!", e);
        }
        return Collections.emptyMap();
    }

    /**
     * from the key-value generated json  to get the str value no matter the real type of value
     *
     * @param json     the json str
     * @param nodeName key
     * @return the str value of key
     */
    public static String getNodeString(String json, String nodeName) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode jsonNode = rootNode.findValue(nodeName);
            if (Objects.isNull(jsonNode)) {
                return null;
            }
            return jsonNode.isTextual() ? jsonNode.asText() : jsonNode.toString();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * json to object
     *
     * @param json json string
     * @param type type reference
     * @param <T>
     * @return return parse object
     */
    public static <T> T parseObject(String json, TypeReference<T> type) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            logger.error("json to map exception!", e);
        }
        return null;
    }

    /**
     * json字符串转ObjectNode
     *
     * @param text
     * @return
     */
    public static ObjectNode parseObject(String text) {
        try {
            if (text.isEmpty()) {
                return parseObject(text, ObjectNode.class);
            } else {
                return (ObjectNode) objectMapper.readTree(text);
            }
        } catch (Exception e) {
            throw new RuntimeException("String json deserialization exception.", e);
        }
    }

    /**
     * json字符串转ObjectNode
     *
     * @param text
     * @return
     */
    public static ObjectNode parseObj(String text) {
        return parseObject(text);
    }

    public static ArrayNode parseArray(String text) {
        try {
            return (ArrayNode) objectMapper.readTree(text);
        } catch (Exception e) {
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }

    /**
     * serialize to json byte
     *
     * @param obj object
     * @param <T> object type
     * @return byte array
     */
    public static <T> byte[] toJsonByteArray(T obj) {
        if (obj == null) {
            return null;
        }
        String json = "";
        try {
            json = toJsonString(obj);
        } catch (Exception e) {
            logger.error("json serialize exception.", e);
        }
        return json.getBytes(UTF_8);
    }

    /**
     * json serializer
     */
    public static class JsonDataSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeRawValue(value);
        }
    }

    /**
     * json data deserializer
     */
    public static class JsonDataDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            if (node instanceof TextNode) {
                return node.asText();
            } else {
                return node.toString();
            }
        }
    }

}
