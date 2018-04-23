import java.util.Map;

public class SmartListModel {
    private String docketName;
    private String docketInfoUid;
    private Integer quantity;
    private String uid;
    private Map<String,Boolean> user;

    public String getDocketName() {
        return docketName;
    }

    public void setDocketName(String docketName) {
        this.docketName = docketName;
    }

    public String getDocketInfoUid() {
        return docketInfoUid;
    }

    public void setDocketInfoUid(String docketInfoUid) {
        this.docketInfoUid = docketInfoUid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Boolean> getUser() {
        return user;
    }

    public void setUser(Map<String, Boolean> user) {
        this.user = user;
    }
}
