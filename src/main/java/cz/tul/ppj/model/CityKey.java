package cz.tul.ppj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CityKey implements Serializable {

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "stateid", foreignKey = @ForeignKey(name = "FK_CITY_STATE"))
    private State state;

    @NotBlank
    @Column(name = "name")
    private String name;

    //

    public State getState() {
        return state;
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

    //

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CityKey cityKey = (CityKey) o;
        return Objects.equals(state, cityKey.state) && Objects.equals(name, cityKey.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, name);
    }

    //

    public CityKey() {
    }

    public CityKey(State state, String name) {
        this.state = state;
        this.name = name;
    }

}
