package com.campaign;

import java.util.HashMap;

import com.campaign.service.PromotionService;

public class PromotionServiceApplication {
	

	public static void main(String[] args) {
		HashMap<String, Integer> cart = new HashMap<>();
		cart.put("A", 3);
		cart.put("B", 5);
		cart.put("C", 1);		
		cart.put("D", 1);
		PromotionService service = new PromotionService();
		int total=service.checkout(cart);
		System.out.println("Cart Cost : " + total);
	}

}
