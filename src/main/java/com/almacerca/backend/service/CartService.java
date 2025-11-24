package com.almacerca.backend.service;

import com.almacerca.backend.dto.CartItemDto;
import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.CartItem;
import com.almacerca.backend.model.Product;
import com.almacerca.backend.model.User;
import com.almacerca.backend.repository.CartItemRepository;
import com.almacerca.backend.repository.ProductRepository;
import com.almacerca.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	public List<CartItemDto> getCartForUser(Long userId) {
		ensureUserExists(userId);
		List<CartItem> items = cartItemRepository.findByUser_Id(userId);
		return items.stream().map(this::toDto).collect(Collectors.toList());
	}

	public CartItem addToCart(Long userId, Long productId, Integer quantity) {
		User user = ensureUserExists(userId);
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		CartItem existing = cartItemRepository.findByUser_IdAndProduct_Id(userId, productId).orElse(null);
		if (existing != null) {
			existing.setQuantity(existing.getQuantity() + (quantity != null ? quantity : 1));
			return cartItemRepository.save(existing);
		}

		CartItem ci = new CartItem();
		ci.setUser(user);
		ci.setProduct(product);
		ci.setQuantity(quantity != null ? quantity : 1);
		return cartItemRepository.save(ci);
	}

	public void removeItem(Long userId, Long productId) {
		ensureUserExists(userId);
		CartItem existing = cartItemRepository.findByUser_IdAndProduct_Id(userId, productId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
		cartItemRepository.delete(existing);
	}

	private User ensureUserExists(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	private CartItemDto toDto(CartItem ci) {
		return new CartItemDto(ci.getId(), ci.getProduct().getId(), ci.getQuantity());
	}

}

