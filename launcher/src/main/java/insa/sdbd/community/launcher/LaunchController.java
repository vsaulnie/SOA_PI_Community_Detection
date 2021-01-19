package insa.sdbd.community.launcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.*;
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

	@GetMapping(path = "/pong", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPong() {
		return "Pong from Launcher Service";
	}

	@GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPing() {
		return restTemplate.getForObject("http://storerServicePI/storer/pong", String.class);
	}

	//It accepts GZ file on distant or local url
	@PostMapping(path = "/loadGZGraphFromURL", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getGZGraph(@RequestBody DownloadInformation information) {
		String res = "No graph loaded (csv)";
		String giraphRes = "No graph loaded (jlld)";
		String status = "KO";
		String decodedURL;
		try {
			decodedURL = URLDecoder.decode(information.getUrl(), StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}

		URL url = null;
		try {
			url = new URL(decodedURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			GZIPInputStream gzipStream = new GZIPInputStream(url.openStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(gzipStream));
			res = "";
			String line = null;
			GiraphFileBuilder giraphFileBuilder = new GiraphFileBuilder();
			while ((line = br.readLine()) != null) {
				res += line.replaceAll(" ", ",") + "\n";
				giraphFileBuilder.readTxtLine(line);
				status = "Locally loaded";
			}
			giraphRes = giraphFileBuilder.toRawString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String fName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1, url.getPath().lastIndexOf(".")) + ".csv";
			FileManager.saveGraphLocally(fName, res, "csv");
			FileManager.saveGraphLocally(fName, giraphRes, "jlld");
			//Neo4JManager.initLoadCSVRequest(fName);
			//prepareForGiraph(res);

		} catch (Neo4JException neo) {
			neo.printStackTrace();
		}
		Json json = new Json();
		json.add("status", status);
		json.add("response", res);
		return json.toString();
	}

	//It accepts GZ file on distant or local url
	@PostMapping(path = "/loadTXTGraphFromURL", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getTXTGraph(@RequestBody DownloadInformation information) {
		String res = "No csv";
		String giraphRes = "No jlld";
		String decodedURL;
		try {
			decodedURL = URLDecoder.decode(information.getUrl(), StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}

		URL url = null;
		try {
			url = new URL(decodedURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(url.openStream()));
			GiraphFileBuilder giraphFileBuilder = new GiraphFileBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				res += line + "\n";

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

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
	@PostMapping(path = "/sendQuery/Neo4j", produces = MediaType.TEXT_PLAIN_VALUE)
	public String sendQueryToNeo4j(@PathVariable("statment") String statmentEncoded) {
		//1° BUILD QUERY - PREPARE POST MESSAGE
		String statment;
		try {
			statment = URLDecoder.decode(statmentEncoded, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
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


}
