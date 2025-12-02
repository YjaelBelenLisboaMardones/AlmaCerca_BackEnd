package com.almacerca.backend.controller;

import com.almacerca.backend.model.Product;
import com.almacerca.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductPublicController {

    @Autowired
    private ProductService productService;

    // ✅ ESTE ES EL QUE USA LA APP DEL CLIENTE
    @GetMapping
    public List<Product> getPublicProducts() {
        return productService.findAllFromDefaultStore();
    }
}
