package cz.tul.ppj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "State")
public class State {

    @Id
    @Column(name = "id")
    private String stateId;

    @Column(name = "name")
    private String name;

    @JsonIgnore
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

    //

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(stateId, state.stateId) && Objects.equals(name, state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateId, name);
    }

}
