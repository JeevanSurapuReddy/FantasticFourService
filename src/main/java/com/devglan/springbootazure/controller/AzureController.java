package com.devglan.springbootazure.controller;

import com.devglan.springbootazure.AzureBlobAdapter;
import com.devglan.springbootazure.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/")
public class AzureController {

    @Autowired
    private AzureBlobAdapter azureBlobAdapter;

    @PostMapping("/container")
    public ResponseEntity createContainer(@RequestBody String containerName){
        boolean created = azureBlobAdapter.createContainer(containerName);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity placeOrder(@RequestBody Product product){
        URI url = azureBlobAdapter.upload(product);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity getAllOrders(@RequestParam String containerName){
        List<URI> uris = azureBlobAdapter.listBlobs(containerName);
        return ResponseEntity.ok(uris);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String containerName, @RequestParam String blobName){
        azureBlobAdapter.deleteBlob(containerName, blobName);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/myendpoint")
    public String sampleData() {
    	return "Check the endpoint";
    }


}
