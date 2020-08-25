package com.example.eureka_client.config.fegin;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FeginClientErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String s, Response response) {

		System.out.println("进入异常解码器");
		try {
			String result = Util.toString(response.body().asReader());
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Exception();
	}
}
