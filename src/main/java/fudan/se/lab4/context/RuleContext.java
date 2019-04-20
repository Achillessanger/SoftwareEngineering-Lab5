package fudan.se.lab4.context;

import fudan.se.lab4.dto.Order;

public class RuleContext {
    //商品列表
    private Order order;
    //客户id
    private int userId;
    //优惠券码
    private int codeCoup;
    RuleContext(Order order,int userId,int codeCoup) {
        this.order=order;
        this.userId=userId;
        this.codeCoup=codeCoup;
    }

    public int getCodeCoup() {
        return codeCoup;
    }

    public int getUserId() {
        return userId;
    }

    public Order getOrder() {
        return order;
    }
}
