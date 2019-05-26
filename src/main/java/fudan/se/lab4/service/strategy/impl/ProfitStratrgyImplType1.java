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

public class ProfitStratrgyImplType1 implements ProfitStrategy {
    public RuleResult profitProcess(RuleContext ruleContext, Rule rule) {
        //TODO 相当于原来的RuleServiceImpl.java里的private RuleResult discountType1(RuleContext ruleContext, Rule rule) 满赠
        String description = "";
        double discount = 0.0;
        Order order = ruleContext.getOrder();
        Map<Drinks, Integer> sendDrinks = new HashMap<>();
        DrinkRepository drinkUtil = new DrinkRepositoryImpl();
        //对全部订单生效，无条件赠送
        if (rule.getDiscountRange() == null || rule.getDiscountRange().size() == 0) {
            if (rule.getFreeDrinks() != null) {
                //任选默认选最贵的，所以send里面只有一个饮品
                for (Rule.Send send : rule.getFreeDrinks()) {
                    sendDrinks.put(send.getDrinksList().get(0), (int) send.getNumber());
                    discount += send.getDrinksList().get(0).getPrice();
                }
            } else {
                return null;
            }
        }
        //有条件的赠送
        List<Rule.ProcessObject> discountRange = rule.getDiscountRange();
        int[] num = new int[discountRange.size()];
        //先记录订单中出现的所有茶的数量 区分size
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

        int max = Integer.MAX_VALUE;
        Map<Drinks, Integer> require = new HashMap<>();
        for (Rule.ProcessObject processObject : discountRange) {
            int index = discountRange.indexOf(processObject);
            for (Drinks drinks : processObject.getDrinksList()) {
                num[index] += drinkNum.get(drinks);
                require.put(drinks, drinkNum.get(drinks));
            }
            int temp = (int) (num[index] / processObject.getNumber());
            if (temp < max) {
                //记录最多送多少次
                max = temp;
            }
        }

        //不为null暂时不考虑了
        if (rule.getFreeDrinks() == null) {
            //默认买三送一
            List<Map.Entry<Drinks, Integer>> sortList = new ArrayList<Map.Entry<Drinks, Integer>>(require.entrySet());
            Collections.sort(sortList, new Comparator<Map.Entry<Drinks, Integer>>() {
                @Override
                public int compare(Map.Entry<Drinks, Integer> o1, Map.Entry<Drinks, Integer> o2) {
                    return (int) o1.getKey().getPrice() * 100 - (int) o2.getKey().getPrice() * 100;
                }
            });

            for (Map.Entry<Drinks, Integer> mapping : sortList) {
                if (max <= 0) {
                    break;
                }
                if (mapping.getValue() - max >= 0) {
                    discount += max * rule.getProfit() * mapping.getKey().getPrice();
                    sendDrinks.put(mapping.getKey(), max);
                    break;
                } else {
                    discount += mapping.getValue() * rule.getProfit() * mapping.getKey().getPrice();
                    sendDrinks.put(mapping.getKey(), max);
                    max = max - mapping.getValue();
                }
            }

        }

        description = (discount == 0.0) ? "" : description;
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
