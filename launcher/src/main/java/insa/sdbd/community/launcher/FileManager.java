package insa.sdbd.community.launcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	public static void saveGraphLocally(String fName , String res) throws Neo4JException {
		
		try {
		      File myObj = new File("csv/"+fName);
		      myObj.createNewFile();
		      FileWriter myWriter = new FileWriter(myObj);
		      myWriter.write(res);
		      myWriter.close();
		      
		    } catch (IOException e) {
		      throw new Neo4JException();
		    }
		
	}
}
