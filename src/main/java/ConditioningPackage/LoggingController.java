package ConditioningPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Hashtable;
import javax.swing.JOptionPane;

public class LoggingController {
    
    Path folderPath;
    String sFolderPath;
    Path filePath;
    public LoggingController(String folderPath) {
        //set the folder path for the logger to place these files
        this.folderPath = Paths.get(folderPath);
        this.sFolderPath = folderPath;
    }

    public void LogFileCreation(String file_name){
        //create file if missing
        if (Files.exists(this.folderPath)){
            System.out.println("path exists");
            this.filePath = Paths.get((this.sFolderPath+file_name));
            if (Files.exists(this.filePath)){
                System.out.println("file path exists");
            }
            else{
                System.out.println("file doesnt exist, creating file");
                try{
                    File file = new File(sFolderPath+file_name);
                    file.createNewFile();
                    System.out.println(String.format("new file: %s, created at path: %s",file_name,this.folderPath));

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        else{
            System.out.println("folder doesnt exists, creating folder");
            try{
                Files.createDirectories(this.folderPath);
                System.out.println("Folder created");
                this.filePath = Paths.get((this.sFolderPath+file_name));
            }catch(IOException e){
                e.printStackTrace();
            }

            if (Files.exists(this.filePath)){
                System.out.println("file path exists");
            }
            else{
                System.out.println("file doesnt exist, creating file");
                try{
                    
                    File file = new File(sFolderPath+file_name);
                    file.createNewFile();
                    System.out.println(String.format("new file: %s, created at path: %s",file_name,this.folderPath));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void Append_To_Log(String text_to_Append){
        // open the file in append mode
        try(FileWriter fw = new FileWriter(this.filePath.toString(),true)){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            fw.write(timestamp+" || "+text_to_Append+"\n");
            System.out.println("Appended to Log");
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "No Tube S# Entered, dumping to Missing File");
            LogFileCreation("MissingSerialNumber");
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void Append_To_Log(Hashtable dict){
            // open the file in append mode
            try(FileWriter fw = new FileWriter(this.filePath.toString(),true)){
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                fw.write(timestamp+" || "+ dict.toString());
                System.out.println("Appended to Log");
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
}



