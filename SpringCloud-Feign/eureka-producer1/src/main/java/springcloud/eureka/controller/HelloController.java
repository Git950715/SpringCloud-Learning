package springcloud.eureka.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/api/hello")
	public String Controller(String name){
		return "hello:"+name+",this is producer1。";
	}
	
}
