package fudan.se.lab4.service.impl;

import fudan.se.lab4.currency.Currency;
import fudan.se.lab4.service.PriceService;

public class PriceServiceImpl implements PriceService {
    public double charge(double rmb, Currency currency) {
        return rmb*currency.getExrate();
    }
}
