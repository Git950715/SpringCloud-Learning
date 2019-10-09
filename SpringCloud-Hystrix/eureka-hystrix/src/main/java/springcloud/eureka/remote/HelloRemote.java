package springcloud.eureka.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springcloud.eureka.remote.hystrix.HelloRemoteHystrix;

@FeignClient(name="eurka-producer",fallback=HelloRemoteHystrix.class)
public interface HelloRemote {

	@RequestMapping(value="/api/hello")
	public String hello(@RequestParam(value = "name")String name);
	
}
