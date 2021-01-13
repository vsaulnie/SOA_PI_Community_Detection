package insa.sdbd.community.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
@RequestMapping("/launcher")
public class LaunchController {
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(path = "/pong", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPong(){
		return "Pong from Launcher Service";
	}

	@GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPing(){
		return restTemplate.getForObject("http://storerServicePI/storer/pong",String.class);
	}
	
	//!\ In the inputed url please use two, yes 2 time the uri encoding method !
	//It accepts GZ file on distant or local url
	@GetMapping(path = "/loadGZGraphFromURL/{url}", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getGZGraph(@PathVariable("url") String encodedURL){
		String res="No graph loaded";
		String status="KO";
		String decodedURL;
		try {
            decodedURL = URLDecoder.decode(encodedURL, StandardCharsets.UTF_8.toString());
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
			 BufferedReader br= new BufferedReader(new InputStreamReader(gzipStream));
			 res="";
			 String line = null;
		        while ((line = br.readLine()) != null) {
				 res+=line.replaceAll(" ", ",")+"\n";
			status="Locally loaded";
		        }
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String fName=url.getPath().substring(url.getPath().lastIndexOf("/")+1, url.getPath().lastIndexOf("."))+".csv";
			Neo4JManager.saveGraphLocally(fName,res);
			Neo4JManager.initLoadCSVRequest(fName);
			//prepareForGiraph(res);
			
		}catch(Neo4JException neo) {
			neo.printStackTrace();
		}
		Json json = new Json();
	    json.add("status", status);
	    json.add("response", res);
		return json.toString();
	}
	
	
	
	//!\ In the inputed url please use two, yes 2 time the uri encoding method !
	//It accepts GZ file on distant or local url
	@GetMapping(path = "/loadTXTGraphFromURL/{url}", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getTXTGraph(@PathVariable("url") String encodedURL){
		String res="";
		String decodedURL;
		try {
			decodedURL = URLDecoder.decode(encodedURL, StandardCharsets.UTF_8.toString());
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
				 	
			String line = null;
			while ((line = br.readLine()) != null) {
				res+=line+"\n";
				
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
		
	}
		
	// Gets automatically the CSV file asked if exits
	@GetMapping(path = "/exposeCSV/{fName}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Object> downloadCSV(@PathVariable("fName") String fName){
		File file = new File("csv/"+fName);
		InputStreamSource resource;
		try {
			resource = new InputStreamResource(new FileInputStream(file));
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			ResponseEntity<Object> responseEntity =ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
			return responseEntity;
		
		} catch (Exception e ) {
			return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);	
		} 
	}
				
}
