package insa.sdbd.services.giraph;

import insa.sdbd.services.giraph.hdprocess.GiraphRun;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static insa.sdbd.services.giraph.GiraphApplication.BASE_HDFS_DIR;

@RestController
@RequestMapping("compute")
public class ComputeController {
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Computation> LaunchComputation(@RequestBody Computation computation){
		GiraphRun giraphRun = GiraphRun.FromComputation(computation, BASE_HDFS_DIR);
		if(giraphRun.isValid()){
			Thread t = new GiraphThread(computation);
			t.start();
			computation.setAlgorithm(giraphRun.repr());
			return ResponseEntity.ok(computation);

		}
		else{
			return ResponseEntity.badRequest().body(computation);
		}

	}

	private class GiraphThread extends Thread {
		GiraphRun giraphRun;
		GiraphThread(Computation computation){
			super();
			this.giraphRun = GiraphRun.FromComputation(computation, BASE_HDFS_DIR);
		}
		@Override
		public void run() {
			try {
				int r = giraphRun.runProcess();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
