package cn.rutui.wechatauth.service;

import cn.rutui.wechatauth.model.TextMessage;
import cn.rutui.wechatauth.util.OpenApiUtils;
import cn.rutui.wechatauth.util.WechatMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TextReplyService {

    private static final String MESSAGE_ID = "MsgId";
    private static final String FROM_USER_NAME = "FromUserName";
    private static final String TO_USER_NAME = "ToUserName";
    private static final String CONTENT = "Content";

    private static final String DEFAULT_REPLY = "invalid question!";

    @Autowired
    RestTemplate restTemplate;

    public static Map<String, Map<Long, String>> CACHE = new ConcurrentHashMap<>(8);

    /**
     * 自动回复文本内容
     *
     * @param requestMap requestMap
     * @return String
     */
    public String reply(Map<String, String> requestMap) {
        String msgId = requestMap.get(MESSAGE_ID);
        String wechatId = requestMap.get(FROM_USER_NAME);
        String gongzhonghaoId = requestMap.get(TO_USER_NAME);

        TextMessage textMessage = new TextMessage(wechatId, gongzhonghaoId);

        String content = requestMap.get(CONTENT);
        String reply = null;
        try {
            if (CACHE.containsKey(msgId)) {
                reply = (String) CACHE.get(msgId).values().toArray()[0];
            } else {
                reply = OpenApiUtils.call(content, restTemplate);

                Map<Long, String> map = new HashMap<>();
                map.put(System.currentTimeMillis(), reply);
                CACHE.put(msgId, map);
            }
        } catch (Exception e) {
            log.error("call openapi error", e);
        }
        if (!StringUtils.hasText(content)) {
            reply = DEFAULT_REPLY;
        }
        textMessage.setContent(reply);

        log.info("REPLY OK: fromUser={},fromUser={},content={},reply={}", wechatId, gongzhonghaoId, content, reply);
        return WechatMessageUtils.textMessageToXml(textMessage);
    }

}
