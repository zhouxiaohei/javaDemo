/**
 * @ClassName JsonUtils
 * @Author JackZhou
 * @Date 2020/3/13  15:31
 **/
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToJsonStr(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) throws IOException {
        return objectMapper.readValue(jsonData, beanType);
    }

}
