package fudan.se.lab4.service;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.RuleResult;

public interface RuleService {
    public RuleResult discount(RuleContext ruleContext);
}
