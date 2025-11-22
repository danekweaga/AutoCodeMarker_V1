/**
 * Write a description of class TestCase here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.File;

public class TestSuiteManager extends Stage 
{
    private final ListView<String> testSuitesListView = new ListView<>();
    private final TextField nameTextField = new TextField();
    String basePath = System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites\\";
    
    //The destination of the test suites
    private File folder;
    public TestSuiteManager() 
    {
        Button createBtn = new Button("Create TestSuite");
        Button updateBtn = new Button("Update TestSuite");
        Button deleteBtn = new Button("Delete");
        nameTextField.setPromptText("Enter name");
        
        // --- ListView (scrollable) ---
        testSuitesListView.setPrefHeight(200);
        //To load existing subfolders
        File baseDir = new File(basePath);
        // Make sure directory exists
        if (!baseDir.exists()) 
        {
            baseDir.mkdirs();
        }
        
        //Get all subfolders
        File[] folders = baseDir.listFiles(File::isDirectory);
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
                
                        
                        
                        
                       
                // Define the target directory path
                String targetDir = (System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites\\"+ name);
                
                // Create the File object for the target directory
                folder = new File(targetDir);
                
                // Check if the folder exists
                if (!folder.exists())
                {
                    // Create the folder and any intermediate directories if necessary
                    if (folder.mkdirs()) 
                    {
                        System.out.println("Folder '" + name + "' created at " + targetDir);
                    } else 
                    {
                        System.out.println("Failed to create folder '" + name + "' at " + targetDir);
                    }
                } else 
                {
                    System.out.println("Folder '" + name + "' already exists at " + targetDir);
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
        
                // Define base path
                String basePath = System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites\\";
        
                // Old folder
                File oldFolder = new File(basePath + oldName);
        
                // New folder
                File newFolder = new File(basePath + newName);
        
                // Attempt rename
                if (oldFolder.exists()) 
                {
                    boolean renamed = oldFolder.renameTo(newFolder);
        
                    if (renamed)
                    {
                        System.out.println("Folder renamed from '" + oldName + "' to '" + newName + "'");
                        // Update ListView
                        testSuitesListView.getItems().set(index, newName);
                    }
                    else
                    {
                        System.out.println("Failed to rename folder '" + oldName + "'");
                    }
                }
                else 
                {
                    System.out.println("Old folder does not exist: " + oldFolder.getAbsolutePath());
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
        
                String basePath = System.getProperty("user.home") + "\\Auto Code Marker\\Test Suites\\";
                File folderToDelete = new File(basePath + name);
        
                if (folderToDelete.exists()) 
                {
                    boolean deleted = deleteFolderRecursively(folderToDelete);
        
                    if (deleted)
                    {
                        System.out.println("Folder '" + name + "' deleted successfully.");
                        testSuitesListView.getItems().remove(index);
                    }
                    else 
                    {
                        System.out.println("Failed to delete folder '" + name + "'.");
                    }
                }
                else 
                {
                    System.out.println("Folder does not exist: " + folderToDelete.getAbsolutePath());
                    testSuitesListView.getItems().remove(index);
                }
            }
        });


        // --- Layout ---
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(
                createBtn,
                updateBtn,
                testSuitesListView,
                nameTextField,
                deleteBtn
        );

        // --- Scene & Stage ---
        Scene scene = new Scene(root, 300, 400);
        setTitle("test suites");
        setScene(scene);    
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

