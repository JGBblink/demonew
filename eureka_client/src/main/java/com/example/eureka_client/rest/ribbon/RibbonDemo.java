package com.example.eureka_client.rest.ribbon;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class RibbonDemo {

	public static void main(String[] args) {
		List<Server> serverList = Arrays.asList(new Server("localhost",9020),new Server("localhost",9021));

		BaseLoadBalancer baseLoadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);
		baseLoadBalancer.setRule(new RandomRule());

		for(int i = 0; i < 5; i++) {
			String first = LoadBalancerCommand.<String>builder().withLoadBalancer(baseLoadBalancer).build()
					.submit(new ServerOperation<String>() {
						@Override
						public Observable<String> call(Server server) {
							String addr = "http://" + server.getHost() + ":" + server.getPort();
							//System.out.println("调用地址" + addr);
							// 使用调用工具进行调用
							return Observable.just("result");
						}
					}).toBlocking().first();

			System.out.println(first);

		}

	}
}
