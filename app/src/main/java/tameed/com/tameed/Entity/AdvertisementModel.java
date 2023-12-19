package tameed.com.tameed.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvertisementModel{

    @SerializedName("ads_id")
    @Expose
    private String adsId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("validity_from")
    @Expose
    private String validityFrom;
    @SerializedName("validity_to")
    @Expose
    private String validityTo;
    @SerializedName("added_date")
    @Expose
    private String addedDate;


    public AdvertisementModel(String adsId, String title, String description, String image, String status, String weight, String validityFrom, String validityTo, String addedDate) {
        super();
        this.adsId = adsId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.status = status;
        this.weight = weight;
        this.validityFrom = validityFrom;
        this.validityTo = validityTo;
        this.addedDate = addedDate;
    }

    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getValidityFrom() {
        return validityFrom;
    }

    public void setValidityFrom(String validityFrom) {
        this.validityFrom = validityFrom;
    }

    public String getValidityTo() {
        return validityTo;
    }

    public void setValidityTo(String validityTo) {
        this.validityTo = validityTo;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

}
