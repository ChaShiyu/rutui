package cn.rutui.wechatauth.model;

import cn.rutui.wechatauth.base.WechatMsgType;
import lombok.Data;

@Data
public class EventMessage extends BaseMessage {

    /**
     * 回复的消息内容
     */
    private String Event;

    public EventMessage(String receiver, String officialWxid) {
        this.setToUserName(receiver);
        this.setFromUserName(officialWxid);
        this.setCreateTime(System.currentTimeMillis());
        this.setMsgType(WechatMsgType.TEXT);
        this.setFuncFlag(0);
    }
}
