package fudan.se.lab4.repository.impl;

import com.csvreader.CsvReader;
import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.repository.RuleRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RuleRepositoryImpl implements RuleRepository {
    DrinkRepositoryImpl drinkRepository = new DrinkRepositoryImpl();
    public class Item{
        protected int requiretType;//0价格 1杯数
        protected double number;
        protected List<Drinks> drinksList;
        public Item(int requiretType,double number,List<Drinks> drinksList) {
            this.requiretType=requiretType;
            this.number=number;
            this.drinksList=drinksList;
        }
        public int getRequiretType() {
            return requiretType;
        }
        public double getNumber() {
            return number;
        }
        public List<Drinks> getDrinksList() {
            return drinksList;
        }
    }

    public List<Rule> getRulesFromCSV(){
        String dataFilePath = FileConstant.SALESRULES_CSV;
        CsvReader reader;
        List<Rule> ret = new ArrayList<>();
        try {
            reader = new CsvReader(dataFilePath, FileConstant.CSV_SEPARATOR, Charset.forName(FileConstant.CHARSET));
            while (reader.readRecord()) {
                String[] item = reader.getValues();
                ret.add(getRule(item));
            }
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Rule getRule(String[] item){
        try {
            int groupId = Integer.parseInt(item[0]);
            int scope = Integer.parseInt(item[1]);
            int profitType = Integer.parseInt(item[2]);
            double profit = Double.parseDouble(item[3]);
            boolean canAdd = (item[4].equals("1"))?true:false;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z", Locale.ENGLISH);
            Date from = (item[5].equals("null"))?null:df.parse(item[5]);
            Date to = (item[6].equals("null"))?null:df.parse(item[6]);
            int isOnlyBasicsDrinks = Integer.parseInt(item[7]);
            return new Rule(groupId,scope,profitType,profit,canAdd,from,to,isOnlyBasicsDrinks,getRuleList(item[8]),getRuleList(item[9]),getRuleList(item[10]),item[11]);
        }catch (ParseException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Item> getRuleList(String str){
        if(str.equals("null")){
            return null;
        }
        List<Item> retList = new ArrayList<>();
        String[] smallStrArr = str.split("@");
        for(String s : smallStrArr){
            String[] args = s.split(":");
            int requireType = Integer.parseInt(args[0]);
            double number = Double.parseDouble(args[1]);
            if(args[2].equals("null")){
                retList.add(new Item(requireType,number,null));
            }else {
                List<Drinks> dkl = new ArrayList<>();
                String[] dklObj = args[2].split("&");
                for(String s2 : dklObj){
                    String[] drinkDes = s2.split("#");
                    for(int i = 1; i < drinkDes.length; i++){
                        Drinks drink = drinkRepository.getDrink(drinkDes[0]);
                        drink.setSize(Integer.parseInt(drinkDes[i]));
                        dkl.add(drink);
                    }
                }
                Item item = new Item(requireType,number,dkl);
                retList.add(item);
            }

        }
        return retList;
    }
}
