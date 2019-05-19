package fudan.se.lab4.context;

import fudan.se.lab4.currency.Currency;
import fudan.se.lab4.entity.Drinks;

import java.util.ArrayList;

import java.util.Locale;
import java.util.ResourceBundle;

public class EnvironmentContext {
    private ResourceBundle bundle=ResourceBundle.getBundle("starbb",new Locale("en","US"));
    private ArrayList<Currency> currencies=new ArrayList<>();
    private ArrayList<String[]> specialDrinks=new ArrayList<>();
    private static final EnvironmentContext environmentContext = new EnvironmentContext();
    private EnvironmentContext(){
        String money=bundle.getString("CURRENCY");
        String[] curArray=money.split(";");
        for (String key:curArray) {
            String[] array=key.split("_");
            currencies.add(new Currency(array[2],array[0],Double.parseDouble(array[1])));
        }
        String special=bundle.getString("SPECIAL");
        String[] speArray=special.split(";");
        for (String key:speArray) {
            String[] array=key.split(",");
            specialDrinks.add(array);
        }

    }
    public static EnvironmentContext getEnvironmentContext(){
        return environmentContext;
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public ArrayList<String[]> getSpecialDrinks() {
        return specialDrinks;
    }
}
