package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.strategy.ProfitStrategy;

import java.util.*;

public class ProfitStrategyImplType2 implements ProfitStrategy {
    public RuleResult profitProcess(RuleContext ruleContext, Rule rule){
        //TODO 相当于原来的RuleServiceImpl.java里的private RuleResult discountType2(RuleContext ruleContext, Rule rule)打折
        String description = "";
        Order order = ruleContext.getOrder();
        DrinkRepository drinkUtil = new DrinkRepositoryImpl();
        double discount = 0.0;
        //对全体对象基础价格打折
        if (rule.getIsOnlyBasicsDrinks() == 0 && (rule.getDiscountRange() == null || rule.getDiscountRange().size() == 0)) {
            discount += ruleContext.getPurePrice()*(1 - rule.getProfit());
        }else {
            //对某些特定商品打折
            List<RuleRepositoryImpl.Item> condition = rule.getOrderCondition();
            int[] num = new int[condition.size()];
            //先记录订单中出现的所有饮品的数量
            Map<String, Integer> drinkNum = new HashMap<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                String key = orderItem.getName()+"#"+orderItem.getSize();
                if (!drinkNum.containsKey(key)) {
                    drinkNum.put(key, 1);
                } else {
                    drinkNum.put(key, drinkNum.get(key) + 1);
                }
            }
            //计算打折次数
            int max = Integer.MAX_VALUE;
            for(RuleRepositoryImpl.Item require1 : condition){
                int index = condition.indexOf(require1);
                for (Drinks drinks : require1.getDrinksList()) {
                    num[index] += drinkNum.get(drinks.getName()+"#"+drinks.getSize());
                }
                int temp = (int) (num[index] / require1.getNumber());
                if (temp < max) {
                    //记录最多送多少次
                    max = temp;
                }
            }
            //开始打折
            List<RuleRepositoryImpl.Item> discountRange = rule.getDiscountRange();
            for (RuleRepositoryImpl.Item processObject : discountRange) {
                //最多送几杯如果不是所有都打这个折扣
                int remain = (int) (max * processObject.getNumber());
                for (Drinks drinks : processObject.getDrinksList()) {
                    if (rule.getIsOnlyBasicsDrinks() == 1) {
                        if (rule.isCanAdd()) {
                            discount += drinks.getPrice() * drinkNum.get(drinks)* (1 - rule.getProfit());
                        } else {
                            if(remain>0){
                                discount += drinks.getPrice() * processObject.getNumber() * drinkNum.get(drinks)* (1 - rule.getProfit());
                                remain--;
                            }else {
                                break;
                            }
                        }
                    } else {

                        if (rule.isCanAdd()) {
                            discount += drinks.cost() *drinkNum.get(drinks)* max * (1 - rule.getProfit());
                        } else {
                            if(remain>0){
                                discount += drinks.cost() * processObject.getNumber() * max * (1 - rule.getProfit());
                                remain--;
                            }else {
                                break;
                            }
                        }
                    }
                }
            }
        }

        return new RuleResult(rule, discount, description);
    }
}
