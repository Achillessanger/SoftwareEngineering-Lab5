package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.service.strategy.ProfitStrategy;

public class ProfitStrategyImplType2 implements ProfitStrategy {
    public RuleResult profitProcess(RuleContext ruleContext, Rule rule){
        return null;
        //TODO 相当于原来的RuleServiceImpl.java里的private RuleResult discountType2(RuleContext ruleContext, Rule rule)
    }
}
