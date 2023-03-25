package org.example;

public interface ActionValidator {
    boolean validate(Action action);

    default ActionValidator and(ActionValidator other) {
        return action -> validate(action) && other.validate(action);
    }
}
