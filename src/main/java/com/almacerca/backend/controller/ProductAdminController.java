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
// 🔥 CAMBIO 1: Quitamos "/admin" de la ruta base. Ahora es pública por defecto.
@RequestMapping("/api/products") 
public class ProductAdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // Este método es tu "Guardia de Seguridad". Lo usaremos solo cuando sea necesario.
    private User requireAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!"ADMIN".equals(user.getRole())) {
            throw new AccessDeniedException("Admin role required");
        }
        return user;
    }

    // 🔥 CAMBIO 2: EL MÉTODO PÚBLICO (GET)
    // Quitamos el requireAdmin() de aquí. Ahora CUALQUIERA puede ver la lista.
    @GetMapping
    public List<Product> getAll() {
        // Ya no pedimos userId ni verificamos rol. Pase quien quiera ver.
        return productRepository.findAll();
    }

    // --- ZONA RESTRINGIDA (Solo Admins) ---

    @PostMapping
    public Product create(@RequestHeader("userId") Long userId, @RequestBody Product product) {
        requireAdmin(userId); // 🔒 Aquí SÍ verificamos
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@RequestHeader("userId") Long userId, @PathVariable Long id, @RequestBody Product updated) {
        requireAdmin(userId); // 🔒 Aquí SÍ verificamos
        Product p = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        p.setName(updated.getName());
        p.setDescription(updated.getDescription());
        p.setPrice(updated.getPrice());
        // p.setImageUrl(updated.getImageUrl()); // <- Recuerda agregar esto si ya pusiste el campo en tu Entidad
        productRepository.save(p);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("userId") Long userId, @PathVariable Long id) {
        requireAdmin(userId); // 🔒 Aquí SÍ verificamos
        Product p = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(p);
        return ResponseEntity.noContent().build();
    }
}