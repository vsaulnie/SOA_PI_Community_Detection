package insa.sdbd.community.storer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/storer")
public class StorerController {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;
	@GetMapping(path = "/pong", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPong(){
		return "Pong from Storer Service";
	}

	@GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPing(){
		return restTemplate.getForObject("http://launcherServicePI/launcher/pong",String.class);
	}
}
