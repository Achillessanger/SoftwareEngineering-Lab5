package fudan.se.lab4.service.impl;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.dto.Ingredient;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.IngredientRepositoryImpl;
import fudan.se.lab4.service.CalWholeDrinkService;
import fudan.se.lab4.util.DrinkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalWholeDrinkServiceImpl implements CalWholeDrinkService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    public Double wholePrice(OrderItem orderItem){
        double price = 0.0;
        Drinks drinks = new DrinkRepositoryImpl().getDrink(orderItem.getName()); //orderItem.getName()
        drinks.setSize(orderItem.getSize());
        price += drinks.cost();
        if(orderItem.getIngredients() == null){
            logger.info(InfoConstant.INVALID_INGREDIENT);
            throw new RuntimeException(InfoConstant.INVALID_INGREDIENT);
        }
        for (Ingredient ingredient : orderItem.getIngredients()) {
            DrinkUtil.isDrinkIngredientValid(ingredient);
            price += new IngredientRepositoryImpl().getIngredient(ingredient.getName()).getPrice() * ingredient.getNumber();
        }
        return price;
    }
}