package cz.tul.ppj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "State")
public class State {

    @Id
    @NotBlank
    @Column(name = "id")
    private String stateId;

    @NotBlank
    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "cityKey.state", orphanRemoval = true)
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

    //

    public State() {
    }

    public State(String stateId, String name) {
        this.stateId = stateId;
        this.name = name;
    }

}
