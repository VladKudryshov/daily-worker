public class ProductInfoModel {
    private String capitalizeName;
    private Long datePaid;
    private String name;
    private Boolean paid;
    private Integer quantity;

    public String getCapitalizeName() {
        return capitalizeName;
    }

    public void setCapitalizeName(String capitalizeName) {
        this.capitalizeName = capitalizeName;
    }

    public Long getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Long datePaid) {
        this.datePaid = datePaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductInfoModel{" +
                "capitalizeName='" + capitalizeName + '\'' +
                ", datePaid=" + datePaid +
                ", name='" + name + '\'' +
                ", paid=" + paid +
                ", quantity=" + quantity +
                '}';
    }
}
