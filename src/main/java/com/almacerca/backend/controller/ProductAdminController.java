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
import com.almacerca.backend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;


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
        return productService.findAllFromDefaultStore();
    }

    @PostMapping
    public Product create(
            @RequestHeader("userId") String userId,
            @RequestBody Product product) {

        requireAdmin(userId);
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @RequestHeader("userId") String userId,
            @PathVariable String id,
            @RequestBody Product updated) {

        requireAdmin(userId);
        Product p = productService.update(id, updated);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("userId") String userId,
            @PathVariable String id) {

        requireAdmin(userId);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
