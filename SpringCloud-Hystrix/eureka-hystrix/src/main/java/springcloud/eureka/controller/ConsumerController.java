package springcloud.eureka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springcloud.eureka.remote.HelloRemote;

@RestController
public class ConsumerController {

	@Autowired
	private HelloRemote helloRemote;
	
	@RequestMapping("/api/consumer/hello")
	public String Controller(String name){
		return helloRemote.hello(name);
	}
	
}
