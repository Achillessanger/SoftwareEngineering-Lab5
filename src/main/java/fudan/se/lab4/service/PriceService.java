package fudan.se.lab4.service;

import fudan.se.lab4.currency.Currency;

public interface PriceService {
    double charge(double rmb, Currency currency) ;
}
