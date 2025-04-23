package cz.tul.ppj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "City")
public class City {

    @EmbeddedId
    private CityKey cityKey;

    @JsonIgnore
    @OneToMany(mappedBy = "weatherKey.city", orphanRemoval = true)
    private Set<Weather> weathers;

    //

    public CityKey getCityKey() {
        return cityKey;
    }

    public void setCityKey(CityKey cityKey) {
        this.cityKey = cityKey;
    }

    public State getState() {
        return cityKey.getState();
    }

    public String getStateId() {
        return cityKey.getState().getStateId();
    }

    public void setState(State state) {
        cityKey.setState(state);
    }

    public String getName() {
        return cityKey.getName();
    }

    public void setName(String name) {
        cityKey.setName(name);
    }

    //

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(cityKey, city.cityKey);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cityKey);
    }

    //

    public City() {
    }

    public City(CityKey cityKey) {
        this.cityKey = cityKey;
    }

}
