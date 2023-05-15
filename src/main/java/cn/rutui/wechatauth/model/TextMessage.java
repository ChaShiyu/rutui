package cn.rutui.wechatauth.model;

import cn.rutui.wechatauth.base.WechatMsgTypeConstant;
import lombok.Data;

@Data
public class TextMessage extends BaseMessage {

    /**
     * 回复的消息内容
     */
    private String Content;

    public TextMessage(String receiver, String officialWxid) {
        this.setToUserName(receiver);
        this.setFromUserName(officialWxid);
        this.setCreateTime(System.currentTimeMillis());
        this.setMsgType(WechatMsgTypeConstant.MESSAGE_TYPE_TEXT);
        this.setFuncFlag(0);
    }
}
