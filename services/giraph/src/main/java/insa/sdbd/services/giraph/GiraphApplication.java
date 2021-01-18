package insa.sdbd.services.giraph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class GiraphApplication {
	public static final String BASE_UPLOAD_DIR = "/home/hduser/upload/";
	public static final String BASE_HDFS_DIR = "/user/hduser/";
	public static final String BASE_RESULTS_DIR = "/home/hduser/results/";
	public static void main(String[] args) {
		SpringApplication.run(GiraphApplication.class, args);
	}
}
