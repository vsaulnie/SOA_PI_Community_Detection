package insa.sdbd.services.giraph;

import insa.sdbd.services.giraph.dockerprocess.DockerExec;
import insa.sdbd.services.giraph.hdprocess.MkdirHDFS;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.io.IOException;

@RestController
@RequestMapping("storage")
public class StorageController {

	@PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Storage StoreGraph(@RequestBody Storage storage,@RequestParam String add){
		System.out.println("Receiving storage request "+((add!=null)?add:"no"));
		return storage;
	}

	@GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
	public String GetGraph(@RequestParam String user,@RequestParam String name){
		String res = "You are asking for graph "+name+" in user "+user;
		System.out.println(res);
		return res;
	}

	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Storage RegisterStorage(@RequestBody Storage storage) throws IOException, InterruptedException {
		MkdirHDFS mkdirHDFS = new MkdirHDFS(storage.getUserStore());
		System.out.println("[Command] "+mkdirHDFS.repr());
		int r = mkdirHDFS.runProcess();
		System.out.println("Exit code : "+r);
		return storage;
	}
}
