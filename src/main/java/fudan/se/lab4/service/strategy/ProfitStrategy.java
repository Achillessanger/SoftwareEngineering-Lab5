package fudan.se.lab4.service.strategy;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;

import java.util.List;

public interface ProfitStrategy {
    /**
     *
     * @param ruleContext
     * @param rule
     * @return RuleResult对象
     */
    RuleResult profitProcess(RuleContext ruleContext, Rule rule,int valid);

    static RuleRepositoryImpl.Item range2Condition(Rule rule, RuleRepositoryImpl.Item processObject){
        for(RuleRepositoryImpl.Item require : rule.getOrderCondition()){
            boolean ifFind = false;
            for(Drinks drink:processObject.getDrinksList()){
                if(contains(drink,require.getDrinksList()))
                    ifFind = true;
                else
                    ifFind = false;
            }
            if(ifFind)
                return require;
        }
        return null;
    }
    static boolean contains(Drinks drink, List<Drinks> list){
        for(Drinks drink1 : list){
            if(drink1.getName().equals(drink.getName()) && drink1.getSize() == drink1.getSize())
                return true;
        }
        return false;
    }

}
