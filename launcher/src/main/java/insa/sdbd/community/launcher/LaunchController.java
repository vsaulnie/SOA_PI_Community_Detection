package insa.sdbd.community.launcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

@RestController
@RequestMapping("/launcher")
public class LaunchController {
	@Autowired
	private RestTemplate restTemplate;
	private String Neo4JAddress;
	private String giraphAddress = "http://giraphServicePI/";
	@GetMapping(path = "/pong", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPong() {
		return "Pong from Launcher Service";
	}

	@GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPing() {
		return restTemplate.getForObject("http://storerServicePI/storer/pong", String.class);
	}

	//It accepts GZ file on distant or local url
	@PostMapping(path = "/loadGraphFromURL", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getGraph(@RequestBody DownloadInformation information) {
		String res = "No graph loaded (csv)";
		String giraphRes = "No graph loaded (jlld)";
		String status = "KO";
		/*String decodedURL;
		try {
			decodedURL = URLDecoder.decode(information.getUrl(), StandardCharsets.UTF_8.toString());
			
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}*/
		String decodedURL = information.getUrl();

		URL url = null;
		try {
			url = new URL(decodedURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String mode = information.getMode();
		System.out.println(mode);
		if(mode.equalsIgnoreCase("gz")) {
			try {
				System.out.println("Mode GZ ");
				GZIPInputStream gzipStream = new GZIPInputStream(url.openStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(gzipStream));
				res = "";
				String line = null;
				GiraphFileBuilder giraphFileBuilder = new GiraphFileBuilder();
				while ((line = br.readLine()) != null) {
					res += line.replaceAll(" ", ",") + "\n";
					giraphFileBuilder.readTxtLine(line);
				}
				giraphRes = giraphFileBuilder.toRawString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Mode not GZ");
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(url.openStream()));
				res="";
				String line = null;
				GiraphFileBuilder giraphFileBuilder = new GiraphFileBuilder();
				while ((line = br.readLine()) != null) {
					res += line + "\n";
					giraphFileBuilder.readTxtLine(line);
				}
				giraphRes = giraphFileBuilder.toRawString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		status = "Locally loaded";

		try {
			String fName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1, url.getPath().lastIndexOf(".")) + ".csv";
			FileManager.saveGraphLocally(fName, res, "csv");
			FileManager.saveGraphLocally(fName, giraphRes, "jlld");

		} catch (Neo4JException neo) {
			neo.printStackTrace();
		}
		Json json = new Json();
		json.add("status", status);
		json.add("response", res);
		return json.toString();
	}

	// Gets automatically the CSV file asked if exits
	@GetMapping(path = "/exposeCSV/{fName}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Object> downloadCSV(@PathVariable("fName") String fName) {
		File file = new File("csv/" + fName);
		InputStreamSource resource;
		try {
			resource = new InputStreamResource(new FileInputStream(file));

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
			return responseEntity;

		} catch (Exception e) {
			return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Gets all CSV files loaded in this MS
	@GetMapping(path = "/displayLoadedCSV/", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getAllLoadedCSV() {
		String status = "KO";
		Json res = new Json();
		String[] pathnames;
		File f = new File("./csv");
		if (f != null) {
			// This filter will only include files ending with .csv
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File f, String name) {
					return name.endsWith(".csv");
				}
			};
			// This is how to apply the filter
			pathnames = f.list(filter);
			int i = 1;
			for (String pathname : pathnames) {
				res.add("file" + i, pathname);
				i++;
			}
			status = "OK";
		}
		System.out.println(res);
		Json json = new Json();
		json.add("status", status);
		json.add("response", res);
		return json.toString();
	}

	// tool to send a query to Neo4J in Cipher language encoded with URIEncoded
	// A query to load a graph to Neo4j is required with LOADCSV
	@PostMapping(path = "/sendQuery/Neo4j", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String sendQueryToNeo4j(@RequestBody CipherQuery_Neo4j cipherQuery) {
		//1° BUILD QUERY - PREPARE POST MESSAGE
		String statment = cipherQuery.getStatment();
		String body = Neo4JManager.buildQuery(statment);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> httpBodyHeader = new HttpEntity<String>(body, headers);

		//2° GET TIME - SEND QUERY - STOP TIME
		String queryRes = restTemplate.postForObject("http://" + Neo4JAddress + "db/data/transaction/commit", httpBodyHeader, String.class);

		//3° INTERPRETATION OF RESULT

		//4° BUILD POST MESSAGE TO STORERMS 

		//5° SEND TI

		return Neo4JManager.sendQuery(statment);
	}

	@PostMapping(path = "/uploadGraph/Giraph",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> postUploadToGiraph(@RequestBody UploadInformation information){
		//Register in giraph storage
		restTemplate.postForEntity(giraphAddress+"storage/register/launcherMS",null,Object.class);
		//Upload file via http form data
		//From Baelrung tutorial https://www.baeldung.com/spring-rest-template-multipart-upload
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
				= new LinkedMultiValueMap<>();
		body.add("file", new File("./jlld/"+information.getFilename()));
		HttpEntity<MultiValueMap<String, Object>> requestEntity
				= new HttpEntity<>(body, headers);

		String concise = information.getFilename().split(".")[0];

		RestTemplate restTemplate = new RestTemplate();
		Object resp = restTemplate.postForEntity(giraphAddress+"storage/upload/launcherMS/"+concise, requestEntity, Object.class);
		return ResponseEntity.ok(resp);
	}

	@PostMapping(path = "/sendQuery/Giraph", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> postLaunchGiraphComputation(@RequestBody GiraphQuery giraphQuery){
		String concise = giraphQuery.getGraph().split(".")[0];
		giraphQuery.setGraph(concise);
		giraphQuery.setUser("launcherMS");
		Object resp = restTemplate.postForEntity(giraphAddress+"compute/",giraphQuery,Object.class);
		return ResponseEntity.ok(resp);
	}


}
