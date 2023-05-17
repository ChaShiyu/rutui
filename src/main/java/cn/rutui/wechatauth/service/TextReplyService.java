package cn.rutui.wechatauth.service;

import cn.rutui.wechatauth.model.TextMessage;
import cn.rutui.wechatauth.util.OpenApiUtils;
import cn.rutui.wechatauth.util.WechatMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class TextReplyService {

    private static final String FROM_USER_NAME = "FromUserName";
    private static final String TO_USER_NAME = "ToUserName";
    private static final String CONTENT = "Content";

    private static final String DEFAULT_REPLY = "invalid question!";

    @Value("${api.openai.secret}")
    String secret;
    @Autowired
    RestTemplate restTemplate;

    /**
     * 自动回复文本内容
     *
     * @param requestMap requestMap
     * @return String
     */
    @Cacheable(value="reply", key="#requestMap.get('Content')", unless = "#result=='invalid question!'")
    public String reply(Map<String, String> requestMap) {
        String wechatId = requestMap.get(FROM_USER_NAME);
        String gongzhonghaoId = requestMap.get(TO_USER_NAME);

        TextMessage textMessage = new TextMessage(wechatId, gongzhonghaoId);

        String content = requestMap.get(CONTENT);
        String reply = null;
        try {
            reply = OpenApiUtils.call(content, secret, restTemplate);
        } catch (Exception e) {
            log.error("call openai error, token={}", secret, e);
        }
        if (!StringUtils.hasText(reply)) {
            reply = DEFAULT_REPLY;
        }
        textMessage.setContent(reply);

        log.info("REPLY OK: fromUser={},fromUser={},content={},reply={}", wechatId, gongzhonghaoId, content, reply);
        return WechatMessageUtils.textMessageToXml(textMessage);
    }

}
