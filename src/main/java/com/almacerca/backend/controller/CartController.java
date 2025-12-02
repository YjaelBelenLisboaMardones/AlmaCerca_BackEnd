package com.almacerca.backend.controller;

import com.almacerca.backend.dto.CartItemDto;
import com.almacerca.backend.exception.AccessDeniedException;
import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.CartItem;
import com.almacerca.backend.model.User;
import com.almacerca.backend.repository.UserRepository;
import com.almacerca.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserRepository userRepository;

	//userId es String en todos los métodos de MongoDB
	private User requireBuyer(String userId) {
		// Nota: userRepository.findById(String) es correcto
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		if (!"BUYER".equals(user.getRole())) {
			throw new AccessDeniedException("Buyer role required");
		}
		return user;
	}

	@GetMapping
	//userId viene en el RequestHeader como String
	public List<CartItemDto> getCart(@RequestHeader("userId") String userId) {
		requireBuyer(userId);
		return cartService.getCartForUser(userId);
	}

	@PostMapping
	public ResponseEntity<CartItem> addToCart(
			//userId es String
			@RequestHeader("userId") String userId,
			//productId es String
			@RequestParam String productId,
			@RequestParam(required = false) Integer quantity) {
		
		requireBuyer(userId);
		//Llamada al servicio con String IDs
		CartItem ci = cartService.addToCart(userId, productId, quantity);
		return ResponseEntity.status(HttpStatus.CREATED).body(ci);
	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<Void> removeItem(
			//userId es String
			@RequestHeader("userId") String userId, 
			//productId en la URL es String
			@PathVariable String productId) {
		
		requireBuyer(userId);
		//Llamada al servicio con String IDs
		cartService.removeItem(userId, productId);
		return ResponseEntity.noContent().build();
	}

}