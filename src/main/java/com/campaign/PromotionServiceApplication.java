package com.campaign;

import com.campaign.service.PromotionService;

public class PromotionServiceApplication {

	public static void main(String[] args) {
		PromotionService service = new PromotionService();
		service.checkout();
	}

}
