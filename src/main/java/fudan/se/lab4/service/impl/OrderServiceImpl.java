package fudan.se.lab4.service.impl;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.dto.*;
import fudan.se.lab4.util.DrinkUtil;

import fudan.se.lab4.entity.Drinks;

import fudan.se.lab4.repository.impl.*;
import fudan.se.lab4.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public PaymentInfo pay(Order order) {
        double price = getPaymentInfoPrice(order);
        double discount = 0.0;
        List<String> msgs;
        PromotionResult promotionResult = (new PromotionServiceImpl()).chooseRules(order,price);
        discount = promotionResult.getDiscount();
        msgs = promotionResult.getPromotionType();
        double discountPrice = price - discount;

        return getPaymentInfo(price, discount, discountPrice, msgs);
    }


    private PaymentInfo getPaymentInfo(double price, double discount, double discountPrice, List<String> msgs) {
        return new PaymentInfo(price, discount, discountPrice, msgs);
    }

    private double getPaymentInfoPrice(Order order) {
        double totalPrice = 0.0;
        for (OrderItem orderItem : order.getOrderItems()) {
            double price = 0.0;
            Drinks drinks = (new DrinkUtil()).getDrinks(orderItem.getName());
            drinks.setSize(orderItem.getSize());
            price += drinks.cost();
            for (Ingredient ingredient : orderItem.getIngredients()) {
                price += new IngredientRepositoryImpl().getIngredient(ingredient.getName()).getPrice() * ingredient.getNumber();
            }
            totalPrice += price;
        }
        return totalPrice;

    }

//    private Drinks getDrinks(String name) {
////        try {
////            Drinks drinks = (Drinks) Class.forName(name).newInstance();
////            return drinks;
////        } catch (ClassNotFoundException e) {
////            logger.info(InfoConstant.FAILED_GET_DRINK);
////            throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
////        } catch (InstantiationException e) {
////            logger.info(InfoConstant.FAILED_GET_DRINK);
////            throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
////        }catch (IllegalAccessException e){
////            logger.info(InfoConstant.FAILED_GET_DRINK);
////            throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
////        }
//
//        switch (name) {
//            case "cappuccino":
//                return new CappuccinoRepositoryImpl().getCappuccino(name);
//            case "espresso":
//                return new EspressoRepositoryImpl().getEspresso(name);
//            case "greenTea":
//                return new GreenTeaRepositoryImpl().getGreenTea(name);
//            case "redTea":
//                return new RedTeaRepositoryImpl().getRedTea(name);
//            default: {
//                logger.info(InfoConstant.FAILED_GET_DRINK);
//                throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
//            }
//        }
//    }
}
