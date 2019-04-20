package fudan.se.lab4.dto;

import java.io.Serializable;
import java.util.List;

public class PaymentInfo implements Serializable {
    private static final long serialVersionUID = -5743364759168621824L;
    private double price;
    private double discount;
    private double discountPrice;
    private List<String> msgs;

    public PaymentInfo(double price, double discount, double discountPrice, List<String> msgs) {
        this.price = price;
        this.discount = discount;
        this.discountPrice = discountPrice;
        this.msgs = msgs;
    }

    public PaymentInfo() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public List<String> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<String> msgs) {
        this.msgs = msgs;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PaymentInfo paymentInfo = (PaymentInfo) obj;
        if (price != paymentInfo.getPrice()) {
            return false;
        }
        if (discount != paymentInfo.getDiscount()) {
            return false;
        }
        if (discountPrice != paymentInfo.getDiscountPrice()) {
            return false;
        }
        if (!msgs.equals(paymentInfo.getMsgs())) {
            return false;
        }
        return true;

    }
}
