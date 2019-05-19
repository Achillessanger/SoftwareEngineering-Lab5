package fudan.se.lab4.service.strategy;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Rule;
import fudan.se.lab4.dto.RuleResult;

public interface ProfitStrategy {
    /**
     *
     * @param ruleContext
     * @param rule
     * @return RuleResult对象
     */
    RuleResult profitProcess(RuleContext ruleContext, Rule rule);
}
