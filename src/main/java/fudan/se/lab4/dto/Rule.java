package fudan.se.lab4.dto;

import fudan.se.lab4.entity.Drinks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Rule {
    private int groupId; //优惠分组的组别
    private int scope;  //资格范围，为0代表无限制，所有客户全都可以享受，这一部分可以进一步扩展成资格类型和资格配置
    private int profitType; //利益类型,0是满减，1是满赠，2是打折
    private double profit; //优惠 打8折就是0.8 送1杯就是1
    private boolean canAdd;//是否支持累加
    private int isOnlyBasicsDrinks;
    private Date from;
    private Date to;
    public class Require{
        private int requiretType;//0价格 1杯数
        private double number;
        private List<Drinks> drinksList;
        public Require(int requiretType,double number,List<Drinks> drinksList) {
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

        public void setDrinksList(List<Drinks> drinksList) {
            this.drinksList = drinksList;
        }

        public void setRequiretType(int requiretType) {
            this.requiretType = requiretType;
        }

        public void setNumber(double number) {
            this.number = number;
        }
    }//TODO 是优惠的判断生效条件
    public class ProcessObject{
        private int requiretType;//0价格 1杯数
        private double number;
        private List<Drinks> drinksList;
        public ProcessObject(int requiretType,double number,List<Drinks> drinksList) {
            this.requiretType=requiretType;
            this.number=number;
            this.drinksList=drinksList;
        }

        public double getNumber() {
            return number;
        }

        public int getRequiretType() {
            return requiretType;
        }

        public List<Drinks> getDrinksList() {
            return drinksList;
        }

    }//TODO 没写完 是优惠的对象
    public class Send{
        private int requiretType;//0价格 1杯数
        private double number;
        private List<Drinks> drinksList;
        public Send(int requiretType,double number,List<Drinks> drinksList) {
            this.requiretType=requiretType;
            this.number=number;
            this.drinksList=drinksList;
        }

        public List<Drinks> getDrinksList() {
            return drinksList;
        }

        public int getRequiretType() {
            return requiretType;
        }

        public double getNumber() {
            return number;
        }

    }//TODO 没写完 是赠送的东西
    private List<ProcessObject> discountRange;
    private List<Require> orderCondition;
    private List<Send> freeDrinks; //如果送饮料，送的类型/数量 不送的优惠为null



    public Rule(int groupId,int scope,int profitType,double profit,List<Send> freeDrinks,boolean canAdd, Date from, Date to, List<ProcessObject> discountRange, List<Require> orderCondition,int isOnlyBasicsDrinks) {
        this.groupId = groupId;
        this.scope = scope;
        this.profitType = profitType;
        this.profit = profit;
        this.freeDrinks = freeDrinks;
//        this.canAdd = canAdd;
        this.from = from;
        this.to = to;
        this.discountRange = discountRange;
        this.orderCondition = orderCondition;
        this.isOnlyBasicsDrinks = isOnlyBasicsDrinks;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getScope() {
        return scope;
    }

    public int getProfitType() {
        return profitType;
    }

    public double getProfit() {
        return profit;
    }

    public List<Send> getFreeDrinks() {
        return freeDrinks;
    }

//    public boolean isCanAdd() {
//        return canAdd;
//    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public List<ProcessObject> getDiscountRange() {
        return discountRange;
    }

    public List<Require> getOrderCondition() {
        return orderCondition;
    }

    public int getIsOnlyBasicsDrinks() {
        return isOnlyBasicsDrinks;
    }
}
