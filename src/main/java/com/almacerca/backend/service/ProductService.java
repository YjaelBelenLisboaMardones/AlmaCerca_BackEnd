package com.almacerca.backend.service;

import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.Product;
import com.almacerca.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.almacerca.backend.config.StoreConstants;

import java.util.List;

@Service
public class ProductService {

	private static final String DEFAULT_STORE_ID = "TIENDA_ADMIN_001";

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllFromDefaultStore() {
        return productRepository.findByStoreId(StoreConstants.DEFAULT_STORE_ID);
    }

    //id ahora es String
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Product create(Product product) {
        //Fuerza la tienda por defecto
        product.setStoreId(DEFAULT_STORE_ID);
        return productRepository.save(product);
    }


    //id ahora es String
    public Product update(String id, Product updated) {
        Product p = findById(id);
        p.setName(updated.getName());
        p.setDescription(updated.getDescription());
        p.setPrice(updated.getPrice());
        return productRepository.save(p);
    }

    //id ahora es String
    public void delete(String id) {
        Product p = findById(id);
        productRepository.delete(p);
    }
}
