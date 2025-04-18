package Code;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FilesHandler {
	public ArrayList<File>objFiles;
	
	public FilesHandler() {
		objFiles=new ArrayList<File>();
		objFiles.add(new File("src/cube.obj"));
	}
	public void addFile(File file) {
		objFiles.add(file);
	}
	public ArrayList<String> getModelNames() {
		 ArrayList<String> modelNames = new ArrayList<>();
		 for (File file : objFiles) {
	            // Assuming the model name can be derived from the file name
	            // You can customize this logic based on your file naming convention
	            String modelName = file.getName(); // You might need to parse the file name
	            modelNames.add(modelName);
	        }
		 return modelNames;
	}
//	public void loadFiles(String folderLoad,String folderInsert) {
//		File dataFolder = new File(folderLoad);
//		if(!dataFolder.exists()) {
//			dataFolder.mkdir();
//		}
//		File[] files = dataFolder.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                objFiles.add(file);
//            }
//        }
//	        createProject(folderInsert);
//	        createFilesInProject(folderLoad,folderInsert);
//	}
//	public void createProject(String folderInsert) {
//		File folder =new File("./"+folderInsert);
//		if (folder.mkdir()) {
//			System.out.println("Folder Created Succesfully");
//		}
//		else {
//			System.out.println("Folder Already Exists");
//		}
//	}
//	public void createFilesInProject(String folderLoad,String folderInsert) {
//		for (File i:objFiles) {
//			Path sourcepath = Path.of(folderLoad);
//			Path destinationpath = Path.of(folderInsert);
//			
//	        Path sourceFile = sourcepath.resolve(i.getName());
//	        Path destinationFile = destinationpath.resolve(i.getName());
//	        try {
//	        	if (Files.exists(destinationFile)) {
//	                System.out.println("Destination file already exists. Skipping copy of : "+ i.getName());
//	            } else {
//	                // Use Files.copy() to copy the file and its content
//	                Files.copy(sourceFile, destinationFile);
//	                System.out.println("File copied successfully.");
//	            }
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
