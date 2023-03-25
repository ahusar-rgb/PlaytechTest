package org.example;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    private final Long id;
    private final Long playerId;
    private final List<Action> actions = new ArrayList<>();

    public GameSession(Long id, Long playerId) {
        this.id = id;
        this.playerId = playerId;
    }

    public Long getId() {
        return id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public List<Action> getActions() {
        return actions;
    }
}
