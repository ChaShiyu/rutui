package cn.rutui.wechatauth.service;

import cn.rutui.wechatauth.base.WechatMsgType;
import cn.rutui.wechatauth.util.WechatMessageUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
@Service
public class WechatService {
    @Autowired
    private TextReplyService textReplyService;
    @Autowired
    private EventReplyService eventReplyService;

    public String callback(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");

        try {
            Map<String, String> requestMap = WechatMessageUtils.parseXml(request);
            log.info("parsed xml:{}", JSON.toJSONString(requestMap));
            // 消息类型
            String msgType = requestMap.get("MsgType");
            switch (msgType) {
                case WechatMsgType.TEXT:
                    return textReplyService.reply(requestMap);
                case WechatMsgType.EVENT:
                    return eventReplyService.reply(requestMap);
                default:
                    return textReplyService.reply(requestMap);
            }
        } catch (Throwable e) {
            log.error("parse xml error", e);
        }
        // 不做回复
        return null;
    }

}
