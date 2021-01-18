package insa.sdbd.services.giraph;

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
	public ResponseEntity<String> GetResult(@PathVariable String user, @PathVariable String graph, @PathVariable String algorithm) {
		Computation computation = new Computation();
		computation.setUser(user);
		computation.setGraph(graph);
		computation.setAlgorithm(algorithm);
		GiraphRun giraphRun = GiraphRun.FromComputation(computation, BASE_HDFS_DIR);
		String outputPath = giraphRun.op;
		String localPath = BASE_RESULTS_DIR + computation.getUser() + "/" + computation.getAlgorithm() + "/" + computation.getGraph() + "/";
		try {
			Scanner scan = new Scanner(new FileInputStream(localPath + "report.txt"));
			String res = "";
			String line;
			while (((line = scan.nextLine()) != null)) {
				res += line + "\n";
			}
			return ResponseEntity.ok(res.substring(0, res.length() - 1));
		} catch (FileNotFoundException e) {
			return ResponseEntity.status(HttpStatus.TOO_EARLY).build();
		}

	}

	private void writeReportFile(Computation computation, int returnCode, long duration) {
		File localPath = new File(BASE_RESULTS_DIR + computation.getUser() + "/" + computation.getAlgorithm() + "/" + computation.getGraph() + "/report.txt");
		
		try {
			localPath.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(localPath);
			String text = "";
			text += "CODE:" + returnCode + "\n";
			text += "TIME:" + duration;
			writer.write(text);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			try {
				FileWriter writer = new FileWriter("/user/hduser/err");
				writer.write(e.toString());
				writer.flush();
				writer.close();
				e.printStackTrace();
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}

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
