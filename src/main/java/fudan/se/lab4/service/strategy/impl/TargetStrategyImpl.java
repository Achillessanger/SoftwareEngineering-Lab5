package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.impl.LoggerServiceImpl;
import fudan.se.lab4.service.impl.OrderServiceImpl;
import fudan.se.lab4.service.strategy.TargetStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class TargetStrategyImpl implements TargetStrategy {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private DrinkRepository drinkRepository = new DrinkRepositoryImpl();

    public int isValid(RuleContext ruleContext, Rule rule) {
        return min(isTimeValid(new Date(), rule.getFrom(), rule.getTo())
                , isOrderConditionValid(ruleContext, rule.getOrderCondition(), rule.isCanAdd()));
    }

    private int isTimeValid(Date current, Date from, Date to) {
        if (from != null && to != null) {
            return (current == from || (current.after(from) && current.before(to))) ? 1 : 0;
        }
        return 1;
    }

    private int isOrderConditionValid(RuleContext ruleContext, List<RuleRepositoryImpl.Item> orderCondition, Boolean canAdd) {
        int result = Integer.MAX_VALUE;
        if (orderCondition != null) {
            for (RuleRepositoryImpl.Item require : orderCondition) {
                switch (require.getRequiretType()) {
                    case 0:
                        result = min(result, isPriceValid(ruleContext.getPurePrice(), require.getNumber(), require.getDrinksList()));
                        break;
                    case 1:
                        result = min(result, isDrinksNumValid(ruleContext.getOrder(), require.getDrinksList(), require.getNumber(), canAdd));
                        break;
                    default:
                        //todo
                        logger.info(new LoggerServiceImpl().log(""));
                        break;
                }
                if (result == -1)
                    return -1;
            }
        }
        return result;
    }

    private int isPriceValid(double purePrice, double priceCondition, List<Drinks> drinksList) {
        //todo 价格对应为null
        if (drinksList != null)
            return -1;
        return (purePrice >= priceCondition) ? 1 : -1;
    }

    private int isDrinksNumValid(Order order, List<Drinks> drinksList, double number, boolean canAdd) {
        if (drinksList == null) {
            return order.getOrderItems().size() / (int) number;
        }
        int realNumber = 0;
        for (OrderItem orderItem : order.getOrderItems()) {
            realNumber += drinksList.contains(drinkRepository.getDrink(orderItem.getName())) ? 1 : 0;
        }
        return canAdd ? realNumber / (int) number : 1;
    }

    private int min(int x, int y) {
        return (x > y) ? y : x;
    }
}
