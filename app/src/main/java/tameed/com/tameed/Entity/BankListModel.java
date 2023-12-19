package tameed.com.tameed.Entity;

public class BankListModel {

        String bank_name = "";
        String bankslist_id = "";


    public BankListModel(String bank_name, String bankslist_id) {
        this.bank_name = bank_name;
        this.bankslist_id = bankslist_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBankslist_id() {
        return bankslist_id;
    }

    public void setBankslist_id(String bankslist_id) {
        this.bankslist_id = bankslist_id;
    }
}
