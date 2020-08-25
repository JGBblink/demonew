package com.example.eureka_client.config.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;

import java.util.List;

public class MyRule extends AbstractLoadBalancerRule {
	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
		String clientName = clientConfig.getClientName();
		System.out.println(clientName);
	}

	@Override
	public Server choose(Object key) {
		List<Server> reachableServers = getLoadBalancer().getReachableServers();
		System.out.println("use my rule...");
		return reachableServers.get(0);
	}
}
