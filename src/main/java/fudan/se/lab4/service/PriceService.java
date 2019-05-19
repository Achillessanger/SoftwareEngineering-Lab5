package fudan.se.lab4.service;

import fudan.se.lab4.currency.Currency;

public interface PriceService {
    /**
     *
     * @param rmb 基础价格（以人民币记录）
     * @param currency 需要的货币种类的对象
     * @return 算过汇率的最后价格
     */
    double charge(double rmb, Currency currency) ;
}
