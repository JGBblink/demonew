package com.example.eureka.extend;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

/**
 * 自定义listener
 * @author JGB
 */
//@Configuration
public class MyListener implements ApplicationListener {
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		System.out.println("触发自定义listener: " + event.getTimestamp());
		if (event instanceof ApplicationStartingEvent) {
			ApplicationStartingEvent startingEvent = (ApplicationStartingEvent) event;
			System.out.print(" 扫描完成");
		}
		else if (event instanceof ApplicationEnvironmentPreparedEvent) {
			ApplicationEnvironmentPreparedEvent preparedEvent = (ApplicationEnvironmentPreparedEvent) event;
			System.out.print(" 环境已加载");
		}
		else if (event instanceof ApplicationPreparedEvent) {
			ApplicationPreparedEvent preparedEvent = (ApplicationPreparedEvent) event;
			System.out.print(" 预加载");
		}
		else if (event instanceof ContextClosedEvent
				&& ((ContextClosedEvent) event).getApplicationContext().getParent() == null) {
			System.out.print(" close");
		}
		else if (event instanceof ApplicationFailedEvent) {
			System.out.print("failed");
		}
	}
}
