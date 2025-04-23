package cz.tul.ppj.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.util.StringUtils;

public class CityDTO {

    private String stateId;

    private String cityName;

    //

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    //

    @JsonIgnore
    public boolean isAnyMemberBlank() {
        return StringUtils.isBlank(stateId) || StringUtils.isBlank(cityName);
    }
}
