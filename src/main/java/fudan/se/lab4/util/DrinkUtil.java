package fudan.se.lab4.util;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.impl.CappuccinoRepositoryImpl;
import fudan.se.lab4.repository.impl.EspressoRepositoryImpl;
import fudan.se.lab4.repository.impl.GreenTeaRepositoryImpl;
import fudan.se.lab4.repository.impl.RedTeaRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrinkUtil {
    private static Logger logger = LoggerFactory.getLogger(DrinkUtil.class);
    public static Drinks getDrinks(String name) {
        switch (name) {
            case "cappuccino":
                return new CappuccinoRepositoryImpl().getCappuccino(name);
            case "espresso":
                return new EspressoRepositoryImpl().getEspresso(name);
            case "greenTea":
                return new GreenTeaRepositoryImpl().getGreenTea(name);
            case "redTea":
                return new RedTeaRepositoryImpl().getRedTea(name);
            default: {
                logger.info(InfoConstant.FAILED_GET_DRINK);
                throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
            }
        }
    }
}
