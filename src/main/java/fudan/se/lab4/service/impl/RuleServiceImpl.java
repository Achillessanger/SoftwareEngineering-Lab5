package fudan.se.lab4.service.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Rule;
import fudan.se.lab4.service.RuleService;
import fudan.se.lab4.dto.RuleResult;

import java.util.ArrayList;

public class RuleServiceImpl implements RuleService {
    @Override
    public RuleResult discount(RuleContext ruleContext) {
        int id=ruleContext.getUserId();
        //客户资格类型
        int type=new UserServiceImpl().getType(id);
        //计算选择策略部分
        ArrayList<Rule> rules=new ArrayList<>();
        getRules(rules);
        return chooseRules(type,rules);
    }
    //这一段本来是要与数据库交互，读出本次促销活动包含的促销规则的，
    // 没有数据库，暂时以助教给的方案写死，如果需要扩展的话，在这里添加修改即可
    //先假设卡布奇诺的商品id为1，浓缩咖啡商品id为2，红茶id为3，绿茶id为4,商品其他信息可以调用goodsService接口
    //如果不想用id，你把它改成用名字标识符也行，随你
    public void getRules(ArrayList<Rule> rules) {
        ArrayList<Integer> cap=new ArrayList<Integer>();
        cap.add(1);
        rules.add(new Rule(0x01,0,0,2,0.5,true,cap));
        ArrayList<Integer> esp=new ArrayList<Integer>();
        esp.add(2);
        rules.add(new Rule(0x00,0,0,2,0.8,true,esp));
        ArrayList<Integer> tea=new ArrayList<Integer>();
        tea.add(3);
        tea.add(4);
        rules.add(new Rule(0x00,0,1,3,1,true,tea));
        //加全场优惠
        rules.add(new Rule(0x10,0,0,100,30,true,null));
    }
    //你需要实现这个函数，ruleResult是一个对象，封装促销规则的返回，你自己设计吧
    public RuleResult chooseRules(int user_type,ArrayList<Rule> rules) {
        return null;
    }
}
