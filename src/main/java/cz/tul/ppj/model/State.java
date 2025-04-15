package cz.tul.ppj.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "State")
public class State {

    @Id
    @Column(name = "id")
    private String stateId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "state", orphanRemoval = true)
    private Set<City> cities;

    //

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }
}
