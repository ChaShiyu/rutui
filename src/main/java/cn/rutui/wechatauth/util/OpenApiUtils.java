package cn.rutui.wechatauth.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 签名验证工具类
 */
@Slf4j
public class OpenApiUtils {

    //设置秘钥, 和微信公众平台一致
    public static final String URL = "https://api.openai.com/v1/chat/completions";

    /**
     * @param content      content
     * @param secret       secret
     * @param restTemplate restTemplate
     * @return String
     */
    public static String call(String content, String secret, RestTemplate restTemplate) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", "gpt-3.5-turbo");

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject_1 = new JSONObject();
        jsonObject_1.put("role", "user");
        jsonObject_1.put("content", content);
        jsonArray.add(jsonObject_1);
        jsonObject.put("messages", jsonArray);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + secret);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(jsonObject, headers);

        ResponseEntity<OpenApiResponse> response = restTemplate.postForEntity(URL, requestEntity, OpenApiResponse.class);
        if (response.getBody() == null || response.getBody().choices.size() <= 0) {
            return null;
        }
        return response.getBody().choices.get(0).message.content;
    }

    @Data
    private static class OpenApiResponse {
        String id;
        String created;
        String model;
        List<Choices> choices;

        @Data
        private static class Choices {
            Message message;

            @Data
            private static class Message {
                String role;
                String content;
            }
        }
    }

}