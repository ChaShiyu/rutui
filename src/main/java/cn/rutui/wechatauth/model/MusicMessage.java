package cn.rutui.wechatauth.model;

import lombok.Data;

/**
 * @author C.W
 * @date 2021/11/26 22:21
 * @description 文本消息
 */
@Data
public class MusicMessage extends BaseMessage {

    /**
     * 回复的消息内容
     */
    private String Content;

}
