package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Rule;
import fudan.se.lab4.service.strategy.TargetStrategy;

public class TargetStrategyImpl implements TargetStrategy {
    public boolean isValid(RuleContext ruleContext, Rule rule){
        return false;
        //TODO 判断订单条件/时间条件/...
    }
}
