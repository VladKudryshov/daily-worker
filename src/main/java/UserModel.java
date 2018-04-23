import java.util.List;

public class UserModel {
    String uid;
    String email;
    List<Object> notifications;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Object> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Object> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", notifications=" + notifications +
                '}';
    }
}
