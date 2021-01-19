package insa.sdbd.community.storer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/db")
public class StorerController {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;
	
	@PostMapping(path = "data/transaction/commit",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getPong(@RequestBody String information){
		System.out.println(information);
		return "{\n" + 
				"  \"results\" : [ {\n" + 
				"    \"columns\" : [ \"id(n)\" ],\n" + 
				"    \"data\" : [ {\n" + 
				"      \"row\" : [ 6 ],\n" + 
				"      \"meta\" : [ null ]\n" + 
				"    } ]\n" + 
				"  } ],\n" + 
				"  \"errors\" : [ ]\n" + 
				"}";
	}

	@GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPing(){
		return restTemplate.getForObject("http://launcherServicePI/launcher/pong",String.class);
	}
}
