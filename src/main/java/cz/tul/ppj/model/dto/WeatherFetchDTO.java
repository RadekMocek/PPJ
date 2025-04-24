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
    public String getErrorMessage() {
        if (StringUtils.isBlank(citySelect)) return "Select a city first.";
        if (StringUtils.isBlank(datestamp)) return "Date must be in YYYY-MM-DD format.";
        if (nDays < 1 || nDays > 7) return "Days must be a number between 1 and 7.";
        return null;
    }

}
