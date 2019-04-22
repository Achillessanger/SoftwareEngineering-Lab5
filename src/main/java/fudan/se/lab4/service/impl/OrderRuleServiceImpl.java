package fudan.se.lab4.service.impl;

import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.PromotionResult;
import fudan.se.lab4.service.OrderRuleService;
import fudan.se.lab4.service.OrderService;

public class OrderRuleServiceImpl implements OrderRuleService {
    @Override
    public PromotionResult calFinalDiscount(Order order,double purePrice){
        PromotionServiceImpl promotionService = new PromotionServiceImpl();
        promotionService.initSaleRule();
        return promotionService.chooseRules(order,purePrice);
    }
}
