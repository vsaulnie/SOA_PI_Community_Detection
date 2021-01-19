package insa.sdbd.community.launcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	public static void saveGraphLocally(String fName , String res, String parentDirectory) throws Neo4JException {

		try {
			String formattedParentDirectory = parentDirectory;
			if(!formattedParentDirectory.endsWith("/"))formattedParentDirectory+="/";
			File myObj = new File(formattedParentDirectory + fName);
			myObj.getParentFile().mkdirs();
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter(myObj);
			myWriter.write(res);
			myWriter.close();

		} catch (IOException e) {
			throw new Neo4JException();
		}
	}
}
