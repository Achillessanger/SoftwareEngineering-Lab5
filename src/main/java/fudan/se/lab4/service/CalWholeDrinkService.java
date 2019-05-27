package fudan.se.lab4.service;

import fudan.se.lab4.dto.OrderItem;

public interface CalWholeDrinkService {
    Double wholePrice(OrderItem orderItem);
}
