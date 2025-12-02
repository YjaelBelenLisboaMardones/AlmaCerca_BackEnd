package com.almacerca.backend.controller;

import com.almacerca.backend.exception.AccessDeniedException;
import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.Product;
import com.almacerca.backend.model.User;
import com.almacerca.backend.repository.ProductRepository;
import com.almacerca.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductAdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    //userId ahora es String
    private User requireAdmin(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!"ADMIN".equals(user.getRole())) {
            throw new AccessDeniedException("Admin role required");
        }
        return user;
    }

    //PÚBLICO
    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    //SOLO ADMIN
    @PostMapping
    public Product create(
            @RequestHeader("userId") String userId,
            @RequestBody Product product) {

        requireAdmin(userId);
        return productRepository.save(product);
    }

    //SOLO ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @RequestHeader("userId") String userId,
            @PathVariable String id,
            @RequestBody Product updated) {

        requireAdmin(userId);

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        p.setName(updated.getName());
        p.setDescription(updated.getDescription());
        p.setPrice(updated.getPrice());

        productRepository.save(p);
        return ResponseEntity.ok(p);
    }

    //SOLO ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("userId") String userId,
            @PathVariable String id) {

        requireAdmin(userId);

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(p);
        return ResponseEntity.noContent().build();
    }
}
