package cn.rutui.wechatauth.schedule;

import cn.rutui.wechatauth.service.TextReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CrontabConfig {

    @Scheduled(cron = "0 */5 * * * ?")//设置定时任务执行时间每5秒执行一次
    public void crontabDemo() {
        List<String> msgIdList = new ArrayList<>();
        synchronized (TextReplyService.CACHE) {
            Map<String, Map<Long, String>> cache = TextReplyService.CACHE;
            log.info("Schedule: before  key={}", cache.keySet().toArray());
            cache.forEach((msgId, longStringMap) -> {
                longStringMap.forEach((timestamp, value) -> {
                    if (System.currentTimeMillis() - timestamp > (5 * 60 * 1000)) {
                        msgIdList.add(msgId);
                    }
                });
            });
            log.info("Schedule: expired key={}", msgIdList.toArray());
            msgIdList.forEach(msgId -> {
                cache.remove(msgId);
            });
            log.info("Schedule: after   key={}", cache.keySet().toArray());
        }
    }

}
