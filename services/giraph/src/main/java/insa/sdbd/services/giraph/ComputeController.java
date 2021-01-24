package insa.sdbd.services.giraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import insa.sdbd.services.giraph.hdprocess.GiraphRun;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static insa.sdbd.services.giraph.GiraphApplication.BASE_HDFS_DIR;
import static insa.sdbd.services.giraph.GiraphApplication.BASE_RESULTS_DIR;

@RestController
@RequestMapping("compute")
public class ComputeController {
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Computation> LaunchComputation(@RequestBody Computation computation) {
		GiraphRun giraphRun = GiraphRun.FromComputation(computation, BASE_HDFS_DIR);
		if (giraphRun.isValid()) {
			Thread t = new GiraphThread(computation);
			t.start();
			computation.setAlgorithm(giraphRun.repr());
			return ResponseEntity.ok(computation);

		} else {
			return ResponseEntity.badRequest().body(computation);
		}
	}

	@GetMapping("/{user}/{graph}/{algorithm}")
	public ResponseEntity<Results> GetResult(@PathVariable String user, @PathVariable String graph, @PathVariable String algorithm) {
		Computation computation = new Computation();
		computation.setUser(user);
		computation.setGraph(graph);
		computation.setAlgorithm(algorithm);
		File localPath = new File(BASE_RESULTS_DIR + computation.getUser() + "/" + computation.getAlgorithm() + "/" + computation.getGraph() + "/results.json");
		if(localPath.exists()){
			try {
				Results res = new ObjectMapper().readValue(localPath,Results.class);
				return ResponseEntity.ok(res);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
		else{
			return ResponseEntity.status(HttpStatus.TOO_EARLY).build();
		}
	}

	private void writeReportFile(Computation computation, int returnCode, long duration) {
		File localPath = new File(BASE_RESULTS_DIR + computation.getUser() + "/" + computation.getAlgorithm() + "/" + computation.getGraph() + "/results.json");
		
		try {
			localPath.getParentFile().mkdirs();
			Results results = new Results();
			results.setStatus(String.valueOf(returnCode));
			results.setExecTime(duration);
			results.setPlatform("giraph");
			results.setQuery(computation.getAlgorithm());
			results.getInfos().put("statusType","returnCode");
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(localPath, results);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class GiraphThread extends Thread {
		GiraphRun giraphRun;
		Computation computation;

		GiraphThread(Computation computation) {
			super();
			this.computation = computation.clone();
			this.giraphRun = GiraphRun.FromComputation(computation, BASE_HDFS_DIR);
		}

		@Override
		public void run() {
			try {
				long start = System.currentTimeMillis();
				int r = giraphRun.runProcess();
				long duration = System.currentTimeMillis() - start;
				writeReportFile(this.computation, r, duration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
