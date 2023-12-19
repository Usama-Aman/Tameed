package tameed.com.tameed.Entity;

import java.util.ArrayList;

/**
 * Created by dell on 2/6/2018.
 */

public class New_Order_Entity {
    String name,review_count,review_rating,order_id,customer_id,provider_id,order_description,serving_date,serving_time,order_status,order_cancel_reason,
            order_cost,cancelled_by,order_type,sort_order,latitude,longitude,location,total_fee,last_modified_date,added_date,admin_commission,
            percentage,service_fee,warranty_days,service_id,category_id,category_name,service_name,is_review_given,order_rating,order_reference_number;


    public String getOrder_reference_number() {
        return order_reference_number;
    }

    public void setOrder_reference_number(String order_reference_number) {
        this.order_reference_number = order_reference_number;
    }

    public String getIs_review_given() {
        return is_review_given;
    }

    public void setIs_review_given(String is_review_given) {
        this.is_review_given = is_review_given;
    }

    public String getOrder_rating() {
        return order_rating;
    }

    public void setOrder_rating(String order_rating) {
        this.order_rating = order_rating;
    }

    ArrayList<Order_Pic_Entity> pic_list;

    public ArrayList<Order_Pic_Entity> getPic_list() {
        return pic_list;
    }

    public void setPic_list(ArrayList<Order_Pic_Entity> pic_list) {
        this.pic_list = pic_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(String review_rating) {
        this.review_rating = review_rating;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getOrder_description() {
        return order_description;
    }

    public void setOrder_description(String order_description) {
        this.order_description = order_description;
    }

    public String getServing_date() {
        return serving_date;
    }

    public void setServing_date(String serving_date) {
        this.serving_date = serving_date;
    }

    public String getServing_time() {
        return serving_time;
    }

    public void setServing_time(String serving_time) {
        this.serving_time = serving_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_cancel_reason() {
        return order_cancel_reason;
    }

    public void setOrder_cancel_reason(String order_cancel_reason) {
        this.order_cancel_reason = order_cancel_reason;
    }

    public String getOrder_cost() {
        return order_cost;
    }

    public void setOrder_cost(String order_cost) {
        this.order_cost = order_cost;
    }

    public String getCancelled_by() {
        return cancelled_by;
    }

    public void setCancelled_by(String cancelled_by) {
        this.cancelled_by = cancelled_by;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getAdmin_commission() {
        return admin_commission;
    }

    public void setAdmin_commission(String admin_commission) {
        this.admin_commission = admin_commission;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }

    public String getWarranty_days() {
        return warranty_days;
    }

    public void setWarranty_days(String warranty_days) {
        this.warranty_days = warranty_days;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public static class Order_Pic_Entity{
        String pic_id,order_id,pic_url,pic_1xthumb_url,pic_2xthumb_url,pic_3xthumb_url,pic_type,added_date;

        public String getPic_id() {
            return pic_id;
        }

        public void setPic_id(String pic_id) {
            this.pic_id = pic_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getPic_1xthumb_url() {
            return pic_1xthumb_url;
        }

        public void setPic_1xthumb_url(String pic_1xthumb_url) {
            this.pic_1xthumb_url = pic_1xthumb_url;
        }

        public String getPic_2xthumb_url() {
            return pic_2xthumb_url;
        }

        public void setPic_2xthumb_url(String pic_2xthumb_url) {
            this.pic_2xthumb_url = pic_2xthumb_url;
        }

        public String getPic_3xthumb_url() {
            return pic_3xthumb_url;
        }

        public void setPic_3xthumb_url(String pic_3xthumb_url) {
            this.pic_3xthumb_url = pic_3xthumb_url;
        }

        public String getPic_type() {
            return pic_type;
        }

        public void setPic_type(String pic_type) {
            this.pic_type = pic_type;
        }

        public String getAdded_date() {
            return added_date;
        }

        public void setAdded_date(String added_date) {
            this.added_date = added_date;
        }
    }

}
