package fudan.se.lab4.service.impl;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.dto.Ingredient;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.dto.PaymentInfo;

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
        double discountPrice = price - discount;
        List<String> msgs = new ArrayList<>();

        return getPaymentInfo(price,discount,discountPrice,msgs);
    }


    private PaymentInfo getPaymentInfo(double price, double discount, double discountPrice, List<String> msgs){
        return new PaymentInfo(price,discount,discountPrice,msgs);
    }

    private double getPaymentInfoPrice(Order order){
        double totalPrice = 0.0;
        for (OrderItem orderItem : order.getOrderItems()){
            double price = 0.0;
            Drinks drinks = getDrinks(orderItem.getName());
            drinks.setSize(orderItem.getSize());
            //没有给我杯数默认一杯了
            price += drinks.cost();
            for (Ingredient ingredient : orderItem.getIngredients()){
                price += new IngredientRepositoryImpl().getIngredient(ingredient.getName()).getPrice()*ingredient.getNumber();
            }
            totalPrice +=price;
        }
        return totalPrice;
    }

    private Drinks getDrinks(String name){
        switch (name){
            case "cappuccino": return new CappuccinoRepositoryImpl().getCappuccino(name);
            case "espresso": return new EspressoRepositoryImpl().getEspresso(name);
            case "greenTea": return new GreenTeaRepositoryImpl().getGreenTea(name);
            case "redTea":return new RedTeaRepositoryImpl().getRedTea(name);
            default:{
                logger.info("fail to get drinks.");
                throw new RuntimeException("fail to get drinks.");
            }
        }



    }
}
