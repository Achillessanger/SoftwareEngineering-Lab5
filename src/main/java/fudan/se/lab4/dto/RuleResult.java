package fudan.se.lab4.dto;

public class RuleResult {
    private Rule rule;
    private String ruleDiscription;
    double discount;
    public RuleResult(Rule rule,double discount, String ruleDiscription){
        this.rule = rule;
        this.discount = discount;
        this.ruleDiscription = ruleDiscription;
    }

    public Rule getRule() {
        return rule;
    }

    public String getRuleDiscription() {
        return ruleDiscription;
    }

    public double getDiscount() {
        return discount;
    }
}
