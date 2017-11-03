// MIT License
// Copyright (c) Microsoft Corporation. All rights reserved.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE

package blobQuickstart.blobAzureApp;


import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;


public class AzureApp 
{
	// Define the connection-string with your values
	public static final String storageConnectionString =
	    "DefaultEndpointsProtocol=https;" +
	    "AccountName=<name>;" +
	    "AccountKey=<key>";

	
    public static void main( String[] args )
    {
    	  try {

          	CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
          	
          	CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
  	
  		    // Get a reference to a container.
  		    // The container name must be lower case
  		    CloudBlobContainer container = blobClient.getContainerReference("quickstartblobs");
  	
  		    // Create the container if it does not exist.
  		    container.createIfNotExists();
  	
  		    // Create a permissions object.
  		    BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
  	
  		    // Include public access in the permissions object.
  		    containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
  		    
  	
  		    // Set the permissions on the container.
  		    container.uploadPermissions(containerPermissions);
  		    
  		    //Getting the path to user's myDocuments folder
  			String myDocs = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
  			
  			//Creating a file in the myDocuments folder
  			File sourceFile = new File(myDocs + File.separator + "results.txt");
  			try
  			{
  				Writer output = new BufferedWriter(new FileWriter(sourceFile));
  		        System.out.println("Location of file: " + sourceFile.toString());
  	
  		        output.write("Hello Azure!");
  		        output.close();
  		        
  		        System.out.println("File has been written");
  		    }
  			catch(IOException x) 
  			{
  				System.err.println(x);
  			}
  			
  			//Getting blob reference
  		    CloudBlockBlob blob = container.getBlockBlobReference("results.txt");
  		    
  		    //Creating a FileInputStream as it is necessary for the upload
  		    FileInputStream myFile = new FileInputStream(sourceFile);
  		    
  		    //Creating blob and uploading file to it
  		    blob.upload( myFile, sourceFile.length());
  		    
  		    //Closing FileInputStream
  		    myFile.close();
  		    
  		    //Listing contents of container
  		    for (ListBlobItem blobItem : container.listBlobs()) {
  		        System.out.println("URI of blob is: " + blobItem.getUri());
  		    }
  		
  			// Download blob. In most cases, you would have to retrieve the reference
  		    // to cloudBlockBlob here. However, we created that reference earlier, and 
  			// haven't changed the blob we're interested in, so we can reuse it. 
  			// First, add a _DOWNLOADED before the .txt so you can see both files in your default directory.
  		    blob.downloadToFile(myDocs + File.separator + "results_DOWNLOADED.txt");
  		    
  		    //Creating a reference to the downloaded file so that it can be deleted
  		    File downloadedFile = new File(myDocs + File.separator + "results_DOWNLOADED.txt");
  		    
  		    Scanner sc = new Scanner(System.in);
  		    
  		    System.out.println("The program has completed successfully.");
  		    System.out.println("Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");
  		    
  		    //Pausing for input
  		    sc.nextLine();
  		    
  		    //Deletes container if it exists then deletes files created locally after exiting application
  		    container.deleteIfExists();
  		    downloadedFile.deleteOnExit();
  		    sourceFile.deleteOnExit();
  		    
  		    //Closing scanner
  		    sc.close();
          	    
          } 
          catch (Exception e) 
          {
              System.out.println(e.getMessage());
              e.printStackTrace();
          }
      
    }
}
