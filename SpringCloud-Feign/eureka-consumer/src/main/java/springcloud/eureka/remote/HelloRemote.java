package springcloud.eureka.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="eurka-producer")
public interface HelloRemote {

	@RequestMapping(value="/api/hello")
	public String hello(@RequestParam(value = "name")String name);
	
}
