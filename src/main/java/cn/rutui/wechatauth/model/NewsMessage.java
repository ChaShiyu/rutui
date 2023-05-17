package cn.rutui.wechatauth.model;

import lombok.Data;

@Data
public class NewsMessage extends BaseMessage {

    /**
     * 回复的消息内容
     */
    private String Content;

}
