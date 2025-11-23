/****************************************************************************
 * Class to help user manage test Suites in folders from the app instead
 *
 * @author Aalmin Adeleke, Chuckwunonso Ekweaga, Aniekan Ekarika, Frances Felicidario
 * @version 11/23/2025
 ***************************************************************************/
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javafx.scene.layout.HBox;

public class TestSuiteManager extends Stage 
{
    private final ListView<String> testSuitesListView = new ListView<>();
    private final TextField nameTextField = new TextField();
    String basePath = System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites\\";
    String tempPath = System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites_temp\\";
    
    //The destination of the test suites
    private File folder;
    private File tempFolder;
    
    public TestSuiteManager() 
    {
        Button createBtn = new Button("Create TestSuite");
        createBtn.setPrefWidth(110);
        Button updateBtn = new Button("Update TestSuite");
        updateBtn.setPrefWidth(110);
        HBox buttonBox = new HBox(8,createBtn,updateBtn);
        
        Button deleteBtn = new Button("Delete");
        deleteBtn.setPrefWidth(240);
        Button saveBtn = new Button("Save & Close");
        saveBtn.setPrefWidth(240);
        nameTextField.setPromptText("Enter name");
        
        // Create temporary copy of Test Suites folder
        createTempCopy();
        
        // --- ListView (scrollable) ---
        testSuitesListView.setPrefHeight(200);
        // To load existing subfolders from temp directory
        File tempDir = new File(tempPath);
        // Make sure directory exists
        if (!tempDir.exists()) 
        {
            tempDir.mkdirs();
        }
        
        // Get all subfolders from temp directory
        File[] folders = tempDir.listFiles(File::isDirectory);
        if (folders != null) 
        {
            for (File folder : folders) 
            {
                testSuitesListView.getItems().add(folder.getName());
            }
        }
        
        // --- Button actions ---
        createBtn.setOnAction(e -> 
        {
            String name = nameTextField.getText().trim();
            if (!name.isEmpty()) 
            {
                testSuitesListView.getItems().add(name);
                nameTextField.clear();
                       
                // Define the target directory path in temp folder
                String targetDir = (tempPath + name);
                
                // Create the File object for the target directory
                folder = new File(targetDir);
                
                // Check if the folder exists
                if (!folder.exists())
                {
                    // Create the folder and any intermediate directories if necessary
                    folder.mkdirs();
                }
            }
        });

        updateBtn.setOnAction(e -> 
        {
            int index = testSuitesListView.getSelectionModel().getSelectedIndex();
            String newName = nameTextField.getText().trim();
            if (index != -1 && !newName.isEmpty()) 
            {
                // Get old name
                String oldName = testSuitesListView.getItems().get(index);
        
                // Old folder in temp directory
                File oldFolder = new File(tempPath + oldName);
        
                // New folder in temp directory
                File newFolder = new File(tempPath + newName);
        
                // Attempt rename
                if (oldFolder.exists()) 
                {
                    oldFolder.renameTo(newFolder);
                    testSuitesListView.getItems().set(index, newName);
                }        
                nameTextField.clear();
            }
        });

        deleteBtn.setOnAction(e -> 
        {
            int index = testSuitesListView.getSelectionModel().getSelectedIndex();
        
            if (index != -1) 
            {
                String name = testSuitesListView.getItems().get(index);
                File folderToDelete = new File(tempPath + name);
        
                if (folderToDelete.exists()) 
                {
                    deleteFolderRecursively(folderToDelete);
                    testSuitesListView.getItems().remove(index);
                }
                else {testSuitesListView.getItems().remove(index);}
            }
        });
        
        saveBtn.setOnAction(e -> 
        {
            // Replace original folder with temp folder
            saveChanges();
            // Close the window
            close();
        });

        // Set close request handler to delete temp folder when window is closed
        setOnCloseRequest(e -> 
        {
            // Delete temporary folder without saving changes
            deleteTempFolder();
        });

        // --- Layout ---
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(
                buttonBox,
                nameTextField,
                testSuitesListView,
                deleteBtn,
                saveBtn
        );

        // --- Scene & Stage ---
        Scene scene = new Scene(root, 250, 375);
        setTitle("Test Suites Manager");
        setScene(scene);    
    }
    
    private void createTempCopy() 
    {
        File originalDir = new File(basePath);
        File tempDir = new File(tempPath);
        
        // Delete temp folder if it already exists
        if (tempDir.exists()) {
            deleteFolderRecursively(tempDir);
        }
        
        // Create temp directory
        tempDir.mkdirs();
        
        // Copy all contents from original to temp if original exists
        if (originalDir.exists() && originalDir.isDirectory()) 
        {
            File[] files = originalDir.listFiles();
            if (files != null) 
            {
                for (File file : files) 
                {
                    try 
                    {
                        copyFileOrDirectory(file, new File(tempPath + file.getName()));
                    } catch (IOException e) {
                        System.err.println("Error copying file: " + file.getName());
                    }
                }
            }
        }
    }
    
    private void copyFileOrDirectory(File source, File target) throws IOException 
    {
        if (source.isDirectory()) 
        {
            if (!target.exists()) 
            {
                target.mkdirs();
            }
            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    copyFileOrDirectory(file, new File(target, file.getName()));
                }
            }
        } else 
        {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    private void saveChanges() 
    {
        File originalDir = new File(basePath);
        File tempDir = new File(tempPath);
        
        // Delete original folder
        if (originalDir.exists()) 
        {
            deleteFolderRecursively(originalDir);
        }
        
        // Create original directory
        originalDir.mkdirs();
        
        // Copy all contents from temp to original
        if (tempDir.exists() && tempDir.isDirectory()) 
        {
            File[] files = tempDir.listFiles();
            if (files != null) 
            {
                for (File file : files) 
                {
                    try 
                    {
                        copyFileOrDirectory(file, new File(basePath + file.getName()));
                    } catch (IOException e) {
                        System.err.println("Error copying file: " + file.getName());
                    }
                }
            }
        }
        
        // Delete temp folder after saving
        deleteTempFolder();
    }
    
    private void deleteTempFolder() 
    {
        File tempDir = new File(tempPath);
        if (tempDir.exists()) 
        {
            deleteFolderRecursively(tempDir);
        }
    }
    
    private boolean deleteFolderRecursively(File file) 
    {
        if (file.isDirectory()) 
        {
            File[] children = file.listFiles();
            if (children != null) 
            {
                for (File child : children) 
                {
                    deleteFolderRecursively(child);
                }
            }
        }
        return file.delete();
    }
}