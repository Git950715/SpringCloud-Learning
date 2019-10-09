package springcloud.eureka.remote.hystrix;

import org.springframework.stereotype.Component;

import springcloud.eureka.remote.HelloRemote;

@Component
public class HelloRemoteHystrix implements HelloRemote {

	@Override
	public String hello(String name) {
		return "hello:"+name+",this is default。";
	}

}
