package org.example;

import java.util.ArrayList;
import java.util.List;

public class ValidatorChain {
    private final List<ActionValidator> validators = new ArrayList<>();

    public void addValidator(ActionValidator validator) {
        validators.add(validator);
    }

    public boolean isActionValid(Action action) {
        for (ActionValidator validator : validators) {
            if (!validator.validate(action)) {
                return false;
            }
        }
        return true;
    }
}
