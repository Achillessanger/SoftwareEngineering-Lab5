package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.service.strategy.ProfitStrategy;

public class ProfitStrategyImplType0 implements ProfitStrategy {
    public RuleResult profitProcess(RuleContext ruleContext, Rule rule) {
        //TODO 相当于原来的RuleServiceImpl.java里的private RuleResult discountType0(RuleContext ruleContext, Rule rule) 满减

        double priceBeforeCal = ruleContext.getPurePrice();
        double condition = rule.getDiscountRange().get(0).getNumber();
        int times = (int) (priceBeforeCal / condition);
        double discount = times * rule.getProfit();
        String description = "满" + condition+ "减" + rule.getProfit();
        return new RuleResult(rule, discount, description);
    }
}
