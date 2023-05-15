package cn.rutui.wechatauth.controller;

import cn.rutui.wechatauth.service.WechatService;
import cn.rutui.wechatauth.util.SignCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@RestController
@RequestMapping("/wechat")
@Slf4j
public class AuthController {

    @Autowired
    WechatService wechatService;

    //微信验证token
    @GetMapping(value = "/checkToken", produces = "text/html;charset=utf-8")
    public String checkToken(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        boolean flag = SignCheckUtils.check(timestamp, nonce, signature);
        return flag ? echostr : null;
    }

    //微信消息回调
    @PostMapping(value = "/checkToken")
    public void callback(HttpServletRequest request, HttpServletResponse response, String signature, String timestamp, String nonce) {
        boolean flag = SignCheckUtils.check(timestamp, nonce, signature);
        if (!flag) {
            log.error("SignCheck failed, signature={}", signature);
            return;
        }
        PrintWriter out = null;
        try {
            String respMessage = wechatService.callback(request);
            if (!StringUtils.hasText(respMessage)) {
                respMessage = "发生错误, 不作回复";
            }
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.write(respMessage);
        } catch (Throwable e) {
            log.error("微信发送消息失败", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
