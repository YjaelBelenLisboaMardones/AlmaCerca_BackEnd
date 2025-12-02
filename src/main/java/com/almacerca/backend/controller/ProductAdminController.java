package com.almacerca.backend.controller;

import com.almacerca.backend.model.Product;
import com.almacerca.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.almacerca.backend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    // @Autowired
    // private UserRepository userRepository; // Removido por no usarse

    @Autowired
    private ProductService productService;


    // private User requireAdmin(String userId) { // Removida la función requireAdmin
    //     ...
    // }

    //PÚBLICO (No se necesita cambiar si ya existe ProductPublicController)
    @GetMapping
    public List<Product> getAll() {
        return productService.findAllFromDefaultStore();
    }

    @PostMapping
    // ⚠️ Removido: @RequestHeader("userId") String userId,
    public Product create(
            @RequestBody Product product) {

        // ⚠️ Removido: requireAdmin(userId);
        return productService.create(product);
    }

    @PutMapping("/{id}")
    // ⚠️ Removido: @RequestHeader("userId") String userId,
    public ResponseEntity<Product> update(
            @PathVariable String id,
            @RequestBody Product updated) {

        // ⚠️ Removido: requireAdmin(userId);
        Product p = productService.update(id, updated);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    // ⚠️ Removido: @RequestHeader("userId") String userId,
    public ResponseEntity<Void> delete(
            @PathVariable String id) {

        // ⚠️ Removido: requireAdmin(userId);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}