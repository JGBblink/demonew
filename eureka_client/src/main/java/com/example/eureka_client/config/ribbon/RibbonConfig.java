package com.example.eureka_client.config.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置ribbon和restTemplate结合
 *
 * @author JGB
 */
@Configuration
public class RibbonConfig {

	/**
	 * 加上@LoadBalanced注解,restTemple可以直接使用远程主机名称发起调用
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * 配置自定义的路由选择:两种方式
	 * 1.配置文件中设定具体服务[服务名].ribbon.NFLoadBalancerRuleClassName=自定义类路径
	 * 	例如:eureka-server.ribbon.NFLoadBalancerRuleClassName=com.example.eureka_client.config.ribbon.MyRule
	 * 2.当前方式
	 * @return
	 */
	//@Bean
	public IRule ribbonRule() {
		return new MyRule();
	}
}
