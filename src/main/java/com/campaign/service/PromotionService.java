package com.campaign.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.campaign.model.Promotion;

@Service
public class PromotionService {

	HashMap<String, Promotion> promotionMap = new HashMap<>();

	HashMap<String, String> clubbedPromotionMap = new HashMap<>();
	HashMap<String, Integer> cart = new HashMap<>();

	String Activepromotions = "3:A:130;2:B:45;1:C&D:30";// Qty:ProductName:cost

	HashMap<String, Integer> checkoutCart = new HashMap<>();;

	public void checkout() {
		System.out.println("Starting..");
		cart.put("A", 3);
		cart.put("B", 5);
		cart.put("C", 1);		
		cart.put("D", 1);

		System.out.println("Checking Promotions..");
		String[] allPromotions = Activepromotions.split(";");
		for (String promotion : allPromotions) {
			addToPromotionMap(promotion);
		}
		System.out.println("Applying Promotions to cart..");
		for (Map.Entry<String, Integer> productMap : cart.entrySet()) {
			String product = productMap.getKey();
			int qty = productMap.getValue();
			if (promotionMap.get(product) != null) {
				applyPromotionUpdateCart(cart, promotionMap.get(product));
			} else {
				checkClubbedPromotionForProduct(product);
				addProductForCheckout(product, qty, getCost(product), false);

			}
			
		}
		System.out.println("Applying Clubbed Promotions to cart..");
		applyClubbedPromotions();
		System.out.println(checkoutCart);
		int total = 0;
		for (Integer val : checkoutCart.values()) {
			total = total + val;
		}
		System.out.println("Cart Cost : " + total);
	}

	private void applyClubbedPromotions() {
		if (clubbedPromotionMap.isEmpty()) {
			System.out.println("No Clubbed Promotions..");
			return;
		}
		for (Entry<String, String> productMap : clubbedPromotionMap.entrySet()) {
			String promotionString = productMap.getValue();
			String product = productMap.getKey();
			String[] productList = promotionString.split("&");
			List<String> list = Arrays.asList(productList);
			if (clubbedPromotionMap.get(list.get(0)) != null && clubbedPromotionMap.get(list.get(1)) != null
					&& checkoutCart.get(list.get(0)) != null && checkoutCart.get(list.get(1)) != null) {
				checkoutCart.remove(list.get(0));
				checkoutCart.remove(list.get(1));
				int minClubbedQty = Math.min(cart.get(list.get(0)), cart.get(list.get(1)));
				cart.put(promotionString, minClubbedQty);
				applyPromotionUpdateCart(cart, promotionMap.get(promotionString));
				if ((cart.get(list.get(0)) - minClubbedQty) > 0) {
					cart.put(list.get(0), cart.get(list.get(0)) - minClubbedQty);
					applyPromotionUpdateCart(cart, promotionMap.get(list.get(0)));
				}
				if ((cart.get(list.get(1)) - minClubbedQty) > 0) {
					cart.put(list.get(1), cart.get(list.get(1)) - minClubbedQty);
					applyPromotionUpdateCart(cart, promotionMap.get(list.get(1)));
				}

			}
		}
	}

	private void applyPromotionUpdateCart(HashMap<String, Integer> cart, Promotion promotion) {
		int requiredQty = cart.get(promotion.getProductName());
		boolean promotionFlag = false;
		while (promotion.getQty() <= requiredQty) {
			promotionFlag = true;
			addProductForCheckout(promotion.getProductName(), promotion.getQty(), promotion.getCost(), promotionFlag);
			if (requiredQty - promotion.getQty() >= 0) {
				cart.put(promotion.getProductName(), requiredQty - promotion.getQty());
				requiredQty = requiredQty - promotion.getQty();
			}
			promotionFlag = false;
		}

		if (cart.get(promotion.getProductName()) > 0) {
			addProductForCheckout(promotion.getProductName(), cart.get(promotion.getProductName()),
					getCost(promotion.getProductName()), promotionFlag);
		}
	}

	private int getCost(String productName) {
		HashMap<String, Integer> productCostMap = new HashMap();
		productCostMap.put("A", 50);
		productCostMap.put("B", 30);
		productCostMap.put("C", 20);
		productCostMap.put("D", 15);
		if (productCostMap.get(productName) != null) {
			return productCostMap.get(productName);
		}
		return 0;
	}

	private void addProductForCheckout(String productName, int qty, int cost, boolean promotionFlag) {
		int additionalCost;
		if (promotionFlag) {
			additionalCost = cost;
		} else {
			additionalCost = qty * cost;
		}
		if (checkoutCart.get(productName) != null) {
			int currentPrice = checkoutCart.get(productName);
			checkoutCart.put(productName, currentPrice + additionalCost);
		} else {
			checkoutCart.put(productName, additionalCost);
		}

	}

	private void addToPromotionMap(String promotion) {
		String[] promotionString = promotion.split(":");
		promotionMap.put(promotionString[1], new Promotion(Integer.parseInt(promotionString[0]), promotionString[1],
				Integer.parseInt(promotionString[2])));
	}

	private boolean checkClubbedPromotionForProduct(String productName) {
		Set<Promotion> promotions = promotionMap.entrySet().stream()
				.filter(entry -> entry.getKey().contains(productName)).map(Map.Entry::getValue)
				.collect(Collectors.toSet());
		if (promotions.isEmpty()) {
			return true;
		}
		for (Promotion promotion : promotions) {
			if (clubbedPromotionMap.get(productName)== null) {
				clubbedPromotionMap.put(productName, promotion.getProductName());

			}
		}

		return false;
	}
}