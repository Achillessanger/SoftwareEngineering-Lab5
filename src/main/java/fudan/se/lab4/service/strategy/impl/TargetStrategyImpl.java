package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
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

    public boolean isValid(RuleContext ruleContext, Rule rule) {
        return isTimeValid(new Date(), rule.getFrom(), rule.getTo())
                && isOrderConditionValid(ruleContext, rule.getOrderCondition());
    }

    private boolean isTimeValid(Date current, Date from, Date to) {
        if (from != null && to != null) {
            return current == from || (current.after(from) && current.before(to));
        }
        return true;
    }

    private boolean isOrderConditionValid(RuleContext ruleContext, List<Rule.Require> orderCondition) {
        if (orderCondition != null) {
            for (Rule.Require require : orderCondition) {
                switch (require.getRequiretType()) {
                    case 0:
                        if (!isPriceValid(ruleContext.getPurePrice(), require.getNumber())) {
                            return false;
                        }
                        break;
                    case 1:
                        if (!isDrinksNumValid(ruleContext.getOrder(), require.getDrinksList(), require.getNumber())) {
                            return false;
                        }
                        break;
                    default:
                        //todo
                        logger.info(new LoggerServiceImpl().log(""));
                        break;
                }
            }
        }
        return true;
    }

    private boolean isPriceValid(double purePrice, double priceCondition) {
        return purePrice >= priceCondition;
    }

    private boolean isDrinksNumValid(Order order, List<Drinks> drinksList, double number) {
        int realNumber = 0;
        for (OrderItem orderItem : order.getOrderItems()) {
            realNumber += drinksList.contains(drinkRepository.getDrink(orderItem.getName())) ? 1 : 0;
        }
        return realNumber >= number;
    }
}
