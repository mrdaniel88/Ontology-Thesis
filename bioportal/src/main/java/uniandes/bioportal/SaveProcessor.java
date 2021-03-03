package uniandes.bioportal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SaveProcessor {
	
	public static void save(String filename, String data) throws IOException {
		Path path = Paths.get(Paths.get("").toAbsolutePath().toString(), filename);
		System.out.println(path.toAbsolutePath().toString());
		Files.write(path, data.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);		
	}
	
	public static void save(String filename, String data, boolean append) throws IOException {
		Path path = Paths.get(Paths.get("").toAbsolutePath().toString(), filename);
		System.out.println(path.toAbsolutePath().toString());
		if(Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			
		}
		StandardOpenOption option = append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING;
		Files.write(path, data.getBytes(), option, StandardOpenOption.CREATE);		
	}
	
	


}
