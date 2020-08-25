package com.example.eureka_client.config;

import lombok.Data;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * 健康监控
 * @author JGB
 */
@Component
@Data
public class HealthMonitorConfig extends AbstractHealthIndicator {

	private boolean status;

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		if(status) {
			builder.up();
		}else {
			builder.down();
		}
	}
}
