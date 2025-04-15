package cz.tul.ppj.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "City")
public class City {

    @Id
    @Column(name = "id")
    private int cityId;

    @ManyToOne
    @JoinColumn(name = "stateId")
    private State state;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "city")
    private Set<City> weathers;

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

    public Set<City> getWeathers() {
        return weathers;
    }

    public void setWeathers(Set<City> weathers) {
        this.weathers = weathers;
    }
}
