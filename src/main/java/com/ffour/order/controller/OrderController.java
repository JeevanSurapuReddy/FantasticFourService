package com.ffour.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ffour.order.model.Product;
import com.ffour.order.service.OrderService;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/container")
    public ResponseEntity createContainer(@RequestBody String containerName){
        boolean created = orderService.createContainer(containerName);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity placeOrder(@RequestBody Product product){
        URI url = orderService.upload(product);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity getAllOrders(@RequestParam String containerName){
        List<URI> uris = orderService.listBlobs(containerName);
        return ResponseEntity.ok(uris);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String containerName, @RequestParam String blobName){
        orderService.deleteBlob(containerName, blobName);
        return ResponseEntity.ok().build();
    }
    
}
