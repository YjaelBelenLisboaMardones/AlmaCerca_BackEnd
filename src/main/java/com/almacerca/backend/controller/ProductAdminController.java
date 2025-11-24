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
@RequestMapping("/api/admin/products")
public class ProductAdminController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	private User requireAdmin(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		if (!"ADMIN".equals(user.getRole())) {
			throw new AccessDeniedException("Admin role required");
		}
		return user;
	}

	@GetMapping
	public List<Product> getAll(@RequestHeader("userId") Long userId) {
		requireAdmin(userId);
		return productRepository.findAll();
	}

	@PostMapping
	public Product create(@RequestHeader("userId") Long userId, @RequestBody Product product) {
		requireAdmin(userId);
		return productRepository.save(product);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> update(@RequestHeader("userId") Long userId, @PathVariable Long id, @RequestBody Product updated) {
		requireAdmin(userId);
		Product p = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		p.setName(updated.getName());
		p.setDescription(updated.getDescription());
		p.setPrice(updated.getPrice());
		productRepository.save(p);
		return ResponseEntity.ok(p);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@RequestHeader("userId") Long userId, @PathVariable Long id) {
		requireAdmin(userId);
		Product p = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		productRepository.delete(p);
		return ResponseEntity.noContent().build();
	}

}

