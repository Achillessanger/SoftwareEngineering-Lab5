package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.dto.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.service.strategy.ProfitStrategy;
import fudan.se.lab4.util.DrinkUtil;

import java.util.*;

public class ProfitStrategyImplType2 implements ProfitStrategy {
    public RuleResult profitProcess(RuleContext ruleContext, Rule rule){
        //TODO 相当于原来的RuleServiceImpl.java里的private RuleResult discountType2(RuleContext ruleContext, Rule rule)打折
        String description = "";
        Order order = ruleContext.getOrder();
        DrinkRepository drinkUtil = new DrinkRepositoryImpl();
        double discount = 0.0;
        //对全体对象打折
        if (rule.getIsOnlyBasicsDrinks()==0&&(rule.getDiscountRange() == null || rule.getDiscountRange().size() == 0)) {
            discount += ruleContext.getPurePrice()*(1 - rule.getProfit());
        }else {
            //对某些特定商品打折
            List<Rule.Require> condition = rule.getOrderCondition();
            int[] num = new int[condition.size()];
            //先记录订单中出现的所有饮品的数量
            Map<Drinks, Integer> drinkNum = new HashMap<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                if (!isContainKey(drinkNum,orderItem)) {
                    Drinks drinks = drinkUtil.getDrink(orderItem.getName());
                    drinks.setSize(orderItem.getSize());
                    drinkNum.put(drinks, 1);
                } else {
                    Drinks drinks = drinkUtil.getDrink(orderItem.getName());
                    drinks.setSize(orderItem.getSize());
                    drinkNum.put(drinks, drinkNum.get(drinks) + 1);
                }
            }
            //计算打折次数
            int max = Integer.MAX_VALUE;
//            Map<String, Integer> require = new HashMap<>();
            for(Rule.Require require1 : condition){
                int index = condition.indexOf(require1);
                for (Drinks drinks : require1.getDrinksList()) {
                    num[index] += drinkNum.get(drinks);
//                    require.put(drinks.getName(), drinkNum.get(drinks.getName()));
                }
                int temp = (int) (num[index] / require1.getNumber());
                if (temp < max) {
                    //记录最多送多少次
                    max = temp;
                }
            }
            //开始打折
            List<Rule.ProcessObject> discountRange = rule.getDiscountRange();
            for(Rule.ProcessObject processObject :discountRange ){
                for (Drinks drinks : processObject.getDrinksList()) {
                    if(rule.getIsOnlyBasicsDrinks()==1){
                        discount += drinks.getPrice()*processObject.getNumber()*max*(1-rule.getProfit());
                    }else {
                        discount += drinks.cost()* processObject.getNumber()*max*(1-rule.getProfit());
                    }
                }
            }
        }

        return new RuleResult(rule, discount, description);
    }


    private boolean isContainKey(Map<Drinks,Integer> map,OrderItem orderItem){
        boolean isContain = false;
        Set set = map.entrySet();
        for(Iterator iter = set.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();

            Drinks key = (Drinks) entry.getKey();
            isContain = (key.getName().equals(orderItem.getName()))&&(key.getSize()==orderItem.getSize());
            if (isContain){
                break;
            }
        }
        return isContain;

    }
}
