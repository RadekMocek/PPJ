package cz.tul.ppj;

import cz.tul.ppj.model.State;

public final class Helper {
    private Helper() {
    }

    public static State initializeState(String id, String name) {
        var result = new State();
        result.setStateId(id);
        result.setName(name);
        return result;
    }

}
