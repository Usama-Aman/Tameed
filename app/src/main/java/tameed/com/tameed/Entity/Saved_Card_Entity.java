package tameed.com.tameed.Entity;

/**
 * Created by dell on 2/19/2018.
 */

public class Saved_Card_Entity {
    String card_id,user_id,paypal_card_id,card_number,last_four_digit,cvv,card_pic,name_on_card,expiry_date;

    public String getName_on_card() {
        return name_on_card;
    }

    public void setName_on_card(String name_on_card) {
        this.name_on_card = name_on_card;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPaypal_card_id() {
        return paypal_card_id;
    }

    public void setPaypal_card_id(String paypal_card_id) {
        this.paypal_card_id = paypal_card_id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getLast_four_digit() {
        return last_four_digit;
    }

    public void setLast_four_digit(String last_four_digit) {
        this.last_four_digit = last_four_digit;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCard_pic() {
        return card_pic;
    }

    public void setCard_pic(String card_pic) {
        this.card_pic = card_pic;
    }
}
