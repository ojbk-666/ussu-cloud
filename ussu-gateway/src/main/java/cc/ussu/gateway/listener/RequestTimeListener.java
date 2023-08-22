package cc.ussu.gateway.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Slf4j
@Component
public class RequestTimeListener implements ApplicationListener<ServletRequestHandledEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        if (event.getProcessingTimeMillis() > 1000) {
            log.warn("请求路径：{}，耗时：{}", event.getRequestUrl(), event.getProcessingTimeMillis());
        }
    }
}
