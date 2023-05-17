package cn.rutui.wechatauth.service;

import cn.rutui.wechatauth.base.WechatEventType;
import cn.rutui.wechatauth.model.TextMessage;
import cn.rutui.wechatauth.util.WechatMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EventReplyService {

    private static final String FROM_USER_NAME = "FromUserName";
    private static final String TO_USER_NAME = "ToUserName";
    private static final String EVENT = "Event";

    /**
     * 自动回复文本内容
     *
     * @param requestMap requestMap
     * @return String
     */
    public String reply(Map<String, String> requestMap) {
        String wechatId = requestMap.get(FROM_USER_NAME);
        String gongzhonghaoId = requestMap.get(TO_USER_NAME);

        TextMessage textMessage = new TextMessage(wechatId, gongzhonghaoId);

        String reply = null;
        String event = requestMap.get(EVENT);
        switch (event) {
            case WechatEventType.SUBSCRIBE:
                // 文本消息处理
                reply = "感谢关注,我是查查的AI助手！";
                break;
            case WechatEventType.UNSUBSCRIBE:
                // 文本消息处理
                reply = "感谢关注,欢迎再次关注";
                break;
            default:
                reply = "ok";
                break;
        }
        textMessage.setContent(reply);

        log.info("REPLY OK: fromUser={},fromUser={},event={},reply={}", wechatId, gongzhonghaoId, event, reply);
        return WechatMessageUtils.textMessageToXml(textMessage);
    }

}
