package config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;


/**
 * Server服务唯一的配置入口
 *
 * @author bfs-service
 * @date 2019/10/17
 */
@Slf4j
@Configuration
@Import({RedisConfig.class})
public class Config {
//
//    @Bean
//    @Profile({"!unittest && !local"})
//    AbstractExceptionHandler.ExceptionHandlerHook exceptionHandlerHook(AlertClient alertClient) {
//        return new AbstractExceptionHandler.ExceptionHandlerHook() {
//            @Override
//            public Consumer<AbstractExceptionHandler.ExceptionContext> getExceptionConsumer() {
//                return context -> alertClient.post("未捕获内部异常", "logText: {}", context.dumpRequest(), context.getException());
//            }
//        };
//    }
}
