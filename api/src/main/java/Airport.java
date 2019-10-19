import java.io.Serializable;

public class Airport implements Serializable {

    private String oaci;
    private String denomination;
    private String province;

    public Airport(String oaci, String denomination, String province) {
        this.oaci = oaci;
        this.denomination = denomination;
        this.province = province;
    }

    public String getOaci() {
        return oaci;
    }

    public void setOaci(String oaci) {
        this.oaci = oaci;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Airport{" +
                "oaci='" + oaci + '\'' +
                ", denomination='" + denomination + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}