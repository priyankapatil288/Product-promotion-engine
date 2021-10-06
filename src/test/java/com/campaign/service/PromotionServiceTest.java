package com.campaign.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith({MockitoExtension.class})
class PromotionServiceTest {
	
	@InjectMocks
	PromotionService service;
	
	@Test
	void testCheckoutServiceWithA() {
		HashMap<String, Integer> cart = new HashMap<>();
		cart.put("A", 3);
		cart.put("B", 5);
		cart.put("C", 1);		
		cart.put("D", 1);
		int total=service.checkout(cart);
		assertEquals(280,total);
		
	}
	
	@Test
	void testCheckoutServiceWithDifferentCart() {
		HashMap<String, Integer> cart = new HashMap<>();
		cart.put("A", 1);
		cart.put("B", 1);
		cart.put("C", 1);		
		
		int total=service.checkout(cart);
		assertEquals(100,total);
		
	}

}
