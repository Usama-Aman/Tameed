package tameed.com.tameed.Entity;

import java.util.ArrayList;

/**
 * Created by dell on 2/5/2018.
 */

public class Provider_List_Entity{
   public ArrayList<String> service_name_list;
    ArrayList<String> category_name_list;
    public ArrayList<String> service_id_list;
    ArrayList<String> category_id_list;



    public String user_id,name,email_address,calling_code,mobile_number,combine_mobile,password,gmt_value,latitude,longitude,location,payment_preference,
		device_id,active_status,verify_code,is_verified,login_status,user_type,profile_pic_thumb_url,profile_pic_2xthumb_url,profile_pic_3xthumb_url,
            		profile_cover_pic_1xthumb_url,profile_cover_pic_2xthumb_url,profile_cover_pic_3xthumb_url,push_notification,online_offline_status,

            		profile_pic_url,description ,review_count,review_rating,order_count,country,user_device_type,mobile_visible ,city_to_cover,distance ,user_bank_name ,user_bank_account_number ,user_bank_iban_number ,is_favourite,

            		user_service_id ,sub_service_id ,main_category_id ,service_id ,added_date ,category_name ,service_name ,sub_service_name;

    public String getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(String user_service_id) {
        this.user_service_id = user_service_id;
    }

    public String getSub_service_id() {
        return sub_service_id;
    }

    public void setSub_service_id(String sub_service_id) {
        this.sub_service_id = sub_service_id;
    }

    public String getMain_category_id() {
        return main_category_id;
    }

    public void setMain_category_id(String main_category_id) {
        this.main_category_id = main_category_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
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

    public String getSub_service_name() {
        return sub_service_name;
    }

    public void setSub_service_name(String sub_service_name) {
        this.sub_service_name = sub_service_name;
    }

    ArrayList<Provider_List_Entity.Service> all_service_list;


    public ArrayList<Service> getAll_service_list() {
        return all_service_list;
    }

    public void setAll_service_list(ArrayList<Service> all_service_list) {
        this.all_service_list = all_service_list;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getCalling_code() {
        return calling_code;
    }

    public void setCalling_code(String calling_code) {
        this.calling_code = calling_code;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCombine_mobile() {
        return combine_mobile;
    }

    public void setCombine_mobile(String combine_mobile) {
        this.combine_mobile = combine_mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGmt_value() {
        return gmt_value;
    }

    public void setGmt_value(String gmt_value) {
        this.gmt_value = gmt_value;
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

    public String getPayment_preference() {
        return payment_preference;
    }

    public void setPayment_preference(String payment_preference) {
        this.payment_preference = payment_preference;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getActive_status() {
        return active_status;
    }

    public void setActive_status(String active_status) {
        this.active_status = active_status;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getLogin_status() {
        return login_status;
    }

    public void setLogin_status(String login_status) {
        this.login_status = login_status;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getProfile_pic_thumb_url() {
        return profile_pic_thumb_url;
    }

    public void setProfile_pic_thumb_url(String profile_pic_thumb_url) {
        this.profile_pic_thumb_url = profile_pic_thumb_url;
    }

    public String getProfile_pic_2xthumb_url() {
        return profile_pic_2xthumb_url;
    }

    public void setProfile_pic_2xthumb_url(String profile_pic_2xthumb_url) {
        this.profile_pic_2xthumb_url = profile_pic_2xthumb_url;
    }

    public String getProfile_pic_3xthumb_url() {
        return profile_pic_3xthumb_url;
    }

    public void setProfile_pic_3xthumb_url(String profile_pic_3xthumb_url) {
        this.profile_pic_3xthumb_url = profile_pic_3xthumb_url;
    }

    public String getProfile_cover_pic_1xthumb_url() {
        return profile_cover_pic_1xthumb_url;
    }

    public void setProfile_cover_pic_1xthumb_url(String profile_cover_pic_1xthumb_url) {
        this.profile_cover_pic_1xthumb_url = profile_cover_pic_1xthumb_url;
    }

    public String getProfile_cover_pic_2xthumb_url() {
        return profile_cover_pic_2xthumb_url;
    }

    public void setProfile_cover_pic_2xthumb_url(String profile_cover_pic_2xthumb_url) {
        this.profile_cover_pic_2xthumb_url = profile_cover_pic_2xthumb_url;
    }

    public String getProfile_cover_pic_3xthumb_url() {
        return profile_cover_pic_3xthumb_url;
    }

    public void setProfile_cover_pic_3xthumb_url(String profile_cover_pic_3xthumb_url) {
        this.profile_cover_pic_3xthumb_url = profile_cover_pic_3xthumb_url;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getPush_notification() {
        return push_notification;
    }

    public void setPush_notification(String push_notification) {
        this.push_notification = push_notification;
    }

    public String getOnline_offline_status() {
        return online_offline_status;
    }

    public void setOnline_offline_status(String online_offline_status) {
        this.online_offline_status = online_offline_status;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUser_device_type() {
        return user_device_type;
    }

    public void setUser_device_type(String user_device_type) {
        this.user_device_type = user_device_type;
    }

    public String getMobile_visible() {
        return mobile_visible;
    }

    public void setMobile_visible(String mobile_visible) {
        this.mobile_visible = mobile_visible;
    }

    public String getCity_to_cover() {
        return city_to_cover;
    }

    public void setCity_to_cover(String city_to_cover) {
        this.city_to_cover = city_to_cover;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUser_bank_name() {
        return user_bank_name;
    }

    public void setUser_bank_name(String user_bank_name) {
        this.user_bank_name = user_bank_name;
    }

    public String getUser_bank_account_number() {
        return user_bank_account_number;
    }

    public void setUser_bank_account_number(String user_bank_account_number) {
        this.user_bank_account_number = user_bank_account_number;
    }

    public String getUser_bank_iban_number() {
        return user_bank_iban_number;
    }

    public void setUser_bank_iban_number(String user_bank_iban_number) {
        this.user_bank_iban_number = user_bank_iban_number;
    }

    public String getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
    }

    public ArrayList<String> getService_name_list() {
        return service_name_list;
    }

    public void setService_name_list(ArrayList<String> service_name_list) {
        this.service_name_list = service_name_list;
    }

    public ArrayList<String> getCategory_name_list() {
        return category_name_list;
    }

    public void setCategory_name_list(ArrayList<String> category_name_list) {
        this.category_name_list = category_name_list;
    }

    public ArrayList<String> getService_id_list() {
        return service_id_list;
    }

    public void setService_id_list(ArrayList<String> service_id_list) {
        this.service_id_list = service_id_list;
    }

    public ArrayList<String> getCategory_id_list() {
        return category_id_list;
    }

    public void setCategory_id_list(ArrayList<String> category_id_list) {
        this.category_id_list = category_id_list;
    }

    public  static class Service{
        String user_service_id,sub_service_id,main_category_id,service_id,added_date,category_name,service_name,sub_service_name;

        public String getUser_service_id() {
            return user_service_id;
        }

        public void setUser_service_id(String user_service_id) {
            this.user_service_id = user_service_id;
        }

        public String getSub_service_id() {
            return sub_service_id;
        }

        public void setSub_service_id(String sub_service_id) {
            this.sub_service_id = sub_service_id;
        }

        public String getMain_category_id() {
            return main_category_id;
        }

        public void setMain_category_id(String main_category_id) {
            this.main_category_id = main_category_id;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getAdded_date() {
            return added_date;
        }

        public void setAdded_date(String added_date) {
            this.added_date = added_date;
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

        public String getSub_service_name() {
            return sub_service_name;
        }

        public void setSub_service_name(String sub_service_name) {
            this.sub_service_name = sub_service_name;
        }
    }



}
