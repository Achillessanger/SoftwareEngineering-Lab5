package fudan.se.lab4.dto;

import java.util.ArrayList;

public class Rule {
    //规则类型 将分组涵盖在其中，没有数据库暂时比较可以比较简单的实现，如0Xyz，y代表组别，z代表组内元素处理方式
    //如0x0_代表第一类优惠，0x1_代表第二类优惠，默认这两组优惠互斥，有点类似数据库中的id，但是自带分组信息
    //如额度是两杯，折扣是8折，0x0代表两杯全八折，0x1代表只有第二杯八折，只在满减的时候做这个区分
    private int type;
    //资格范围，为0代表无限制，所有客户全都可以享受，这一部分可以进一步扩展成资格类型和资格配置
    private int scope;
    //对象范围，此类优惠规则针对某类对象，这部分可以扩展为对象范围和对象配置，如只有大杯型的卡布奇诺买几送几等
    //如果是第二类优惠，如全场优惠，我们直接忽略此参数即可
    private ArrayList<Integer> oriented;
    //利益类型，是满减还是满赠等等,0是满减，1是满赠
    private int profit_type;
    //额度，如满200减10中额度为200
    private int credit;
    //优惠
    private double profit;
    //是否支持累加
    private boolean canAdd;
    public Rule(int type,int scope,int profit_type,int credit,double profit,boolean canAdd,ArrayList<Integer> oriented) {
        this.type=type;
        this.scope=scope;
        this.profit_type=profit_type;
        this.credit=credit;
        this.profit=profit;
        this.canAdd=canAdd;
        this.oriented=oriented;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public int getCredit() {
        return credit;
    }

    public double getProfit() {
        return profit;
    }

    public int getProfit_type() {
        return profit_type;
    }

    public int getScope() {
        return scope;
    }

    public int getType() {
        return type;
    }

    public ArrayList<Integer> getOriented() {
        return oriented;
    }
}
