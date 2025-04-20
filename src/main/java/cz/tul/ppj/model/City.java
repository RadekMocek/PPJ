package cz.tul.ppj.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "City")
public class City {

    @Id
    @Column(name = "id")
    private int cityId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stateid")
    private State state;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "weatherKey.city", orphanRemoval = true)
    private Set<Weather> weathers;

    //

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public State getState() {
        return state;
    }

    public String getStateId() {
        return state.getStateId();
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(Set<Weather> weathers) {
        this.weathers = weathers;
    }

    //

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return cityId == city.cityId && Objects.equals(state, city.state) && Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, state, name);
    }

}
