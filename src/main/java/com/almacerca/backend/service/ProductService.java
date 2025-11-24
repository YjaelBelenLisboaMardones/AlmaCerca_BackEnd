package com.almacerca.backend.service;

import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.Product;
import com.almacerca.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Product findById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}

	public Product create(Product product) {
		return productRepository.save(product);
	}

	public Product update(Long id, Product updated) {
		Product p = findById(id);
		p.setName(updated.getName());
		p.setDescription(updated.getDescription());
		p.setPrice(updated.getPrice());
		return productRepository.save(p);
	}

	public void delete(Long id) {
		Product p = findById(id);
		productRepository.delete(p);
	}

}
