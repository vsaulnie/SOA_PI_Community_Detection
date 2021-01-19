package insa.sdbd.community.storer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/storer")
public class StorerController {
	private String giraphAddress = "http://giraphServicePI/";
	private String giraphUser = "launcherMS";
	private String resultsDir = "./results/";
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;

	@GetMapping(path = "/pong", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPong() {
		return "Pong from Storer Service";
	}

	@GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPing() {
		return restTemplate.getForObject("http://launcherServicePI/launcher/pong", String.class);
	}

	@GetMapping(path = "/{platform}/{graph}/{algorithm}")
	public ResponseEntity<Results> getResults(@PathVariable String platform, @PathVariable String graph, @PathVariable String algorithm) {
		Results res = null;
		File file = getResultFile(platform, graph, algorithm);
		ObjectMapper mapper = new ObjectMapper();
		if (file.exists()) {

			try {
				res = mapper.readValue(file, Results.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (platform.toLowerCase().equals("giraph")) {
			res = restTemplate.getForObject(giraphAddress + "compute/" + giraphUser + "/" + graph + "/" + algorithm, Results.class);
			writeResultFile(file, res);

		}

		return (res == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(res);
	}

	@PostMapping(path = "/{graph}/{algorithm}")
	public ResponseEntity postStoreNeo4JResult(@PathVariable String graph, @PathVariable String algorithm, @RequestBody Results results) {
		if (results.getPlatform().toLowerCase().equals("neo4j")) {
			String concise = graph.split("[.]")[0];
			File file = getResultFile("neo4j", graph, algorithm);
			writeResultFile(file, results);
			return ResponseEntity.ok().build();
		} else if (results.getPlatform().toLowerCase().equals("giraph")) {
			return ResponseEntity.badRequest().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private void writeResultFile(File file, Results res) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		res.getInfos().put("service", "storer");
		res.getInfos().put("date", dtf.format(now));
		ObjectMapper mapper = new ObjectMapper();
		try {
			file.getParentFile().mkdirs();
			mapper.writeValue(file, res);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getResultFile(String platform, String graph, String algorithm) {
		String concise = graph.split("[.]")[0].toLowerCase();
		return new File(resultsDir + platform.toLowerCase() + "/" + concise + "/" + algorithm.toLowerCase() + ".json");
	}
}
