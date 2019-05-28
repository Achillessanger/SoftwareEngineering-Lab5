package fudan.se.lab4.context;

import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.currency.Currency;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.repository.RuleRepository;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;

import java.util.*;

public class EnvironmentContext {
    private ResourceBundle bundle=ResourceBundle.getBundle("starbb",new Locale("en","US"));
    private ArrayList<Currency> currencies=new ArrayList<>();
    private ArrayList<String[]> specialDrinks=new ArrayList<>();
    private static final EnvironmentContext environmentContext = new EnvironmentContext();
    private List<Rule> rules;
    private EnvironmentContext(){
        String money=bundle.getString("CURRENCY");
        String[] curArray=money.split(";");
        for (String key:curArray) {
            String[] array=key.split("_");
            currencies.add(new Currency(array[2],array[0],Double.parseDouble(array[1])));
        }
        String special=bundle.getString("SPECIAL");
        if(!special.equals("")){
            String[] speArray=special.split(";");
            for (String key:speArray) {
                String[] array=key.split(",");
                specialDrinks.add(array);
            }
        }

        RuleRepository ruleRepository = new RuleRepositoryImpl();
        this.rules = ruleRepository.getRulesFromCSV(FileConstant.SALESRULES_CSV,bundle);
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

    public List<Rule> getRules() {
        return rules;
    }
}
