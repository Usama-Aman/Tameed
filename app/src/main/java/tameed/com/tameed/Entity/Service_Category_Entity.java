package tameed.com.tameed.Entity;

import java.util.ArrayList;

/**
 * Created by dell on 1/4/2018.
 */

public class Service_Category_Entity {
    String category_id,category_name,added_date,no,type,is_enable,is_subscribed;
    public boolean isSelected;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getIs_subscribed() {
        return is_subscribed;
    }

    public void setIs_subscribed(String is_subscribed) {
        this.is_subscribed = is_subscribed;
    }

    ArrayList<Services_Entity> list=new ArrayList<>();



    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public ArrayList<Services_Entity> getList() {
        return list;
    }

    public void setList(ArrayList<Services_Entity> list) {
        this.list = list;
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

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }


    public static class Services_Entity{
String service_id,category_id,service_name,added_date,no,type,is_enable,is_subscribed;
        public boolean isSubSelected;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIs_enable() {
            return is_enable;
        }

        public void setIs_enable(String is_enable) {
            this.is_enable = is_enable;
        }

        public String getIs_subscribed() {
            return is_subscribed;
        }

        public void setIs_subscribed(String is_subscribed) {
            this.is_subscribed = is_subscribed;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
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

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getAdded_date() {
            return added_date;
        }

        public void setAdded_date(String added_date) {
            this.added_date = added_date;
        }
    }
}
