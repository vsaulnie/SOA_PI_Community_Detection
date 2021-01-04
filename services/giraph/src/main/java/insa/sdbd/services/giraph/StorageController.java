package insa.sdbd.services.giraph;

import insa.sdbd.services.giraph.hdprocess.PutHDFS;
import insa.sdbd.services.giraph.hdprocess.MkdirHDFS;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static insa.sdbd.services.giraph.GiraphApplication.BASE_HDFS_DIR;
import static insa.sdbd.services.giraph.GiraphApplication.BASE_UPLOAD_DIR;

@RestController
@RequestMapping("storage")
public class StorageController {

	@PostMapping(path = "/register/{user}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Storage RegisterStorage(@PathVariable String user) throws IOException, InterruptedException {
		MkdirHDFS mkdirHDFS = new MkdirHDFS(user);
		System.out.println("[Command] "+mkdirHDFS.repr());
		int r = mkdirHDFS.runProcess();
		Path path = Paths.get(BASE_UPLOAD_DIR +user);
		Files.createDirectories(path);
		System.out.println("Exit code : "+r);
		Storage storage = new Storage();
		storage.setUser(user);
		return storage;
	}

	@PostMapping(path = "/upload/{user}/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Storage UploadGraph(@PathVariable String user, @PathVariable String name, @RequestParam("file") MultipartFile file)
			throws IOException, InterruptedException{
		String local = BASE_UPLOAD_DIR +user+"/"+name;
		Path path = Paths.get(local);
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String hdfs = BASE_HDFS_DIR +user+"/"+name;
		PutHDFS copy = new PutHDFS(local,hdfs);
		int r = copy.runProcess();
		Storage storage = new Storage();
		if(r==0){
			storage.setUser(user);
			storage.setGraph(name);
		}
		return storage;
	}

}
