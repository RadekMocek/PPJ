package cz.tul.ppj.model;

public class City {

    private int cityId;

    private State state;

    private String name;

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
}
