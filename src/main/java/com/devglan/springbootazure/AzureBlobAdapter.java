package com.devglan.springbootazure;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.devglan.springbootazure.model.Product;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AzureBlobAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AzureBlobAdapter.class);

    @Autowired
    private CloudBlobClient cloudBlobClient;

    @Autowired
    private CloudBlobContainer cloudBlobContainer;
    
//    public AzureBlobAdapter() {
//    	BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
//    		    .endpoint("<your-storage-account-url>")
//    		    .sasToken("<your-sasToken>")
//    		    .buildClient();
//    }

    public boolean createContainer(String containerName){
        boolean containerCreated = false;
        CloudBlobContainer container = null;
        try {
            container = cloudBlobClient.getContainerReference(containerName);
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (StorageException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        try {
            containerCreated = container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
        } catch (StorageException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return containerCreated;
    }

    public URI upload(Product multipartFile){
        URI uri = null;
        CloudBlockBlob blob = null;
        try {
        	cloudBlobContainer.createIfNotExists();
            blob = cloudBlobContainer.getBlockBlobReference(multipartFile.getId());
            blob.upload(new ByteArrayInputStream(multipartFile.toString().getBytes()), -1);
            uri = blob.getUri();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    public List<URI> listBlobs(String containerName){
        List<URI> uris = new ArrayList<>();
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            for (ListBlobItem blobItem : container.listBlobs()) {
                uris.add(blobItem.getUri());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return uris;
    }

    public void deleteBlob(String containerName, String blobName){
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            CloudBlockBlob blobToBeDeleted = container.getBlockBlobReference(blobName);
            blobToBeDeleted.deleteIfExists();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
}
