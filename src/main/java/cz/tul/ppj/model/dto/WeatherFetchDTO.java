package cz.tul.ppj.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.util.StringUtils;

public class WeatherFetchDTO {

    private String citySelect;

    private String datestamp;

    private int nDays;

    //

    public String getCitySelect() {
        return citySelect;
    }

    public void setCitySelect(String citySelect) {
        this.citySelect = citySelect;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public int getnDays() {
        return nDays;
    }

    public void setnDays(int nDays) {
        this.nDays = nDays;
    }

    //

    @JsonIgnore
    public boolean isValid() {
        return !StringUtils.isBlank(citySelect) && !StringUtils.isBlank(datestamp) && nDays >= 1 && nDays <= 7;
    }

}
