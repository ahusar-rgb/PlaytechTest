package org.example;

import java.util.*;

public class Main {

    private final static String INPUT_FILENAME = "src/main/resources/game_data_1.txt";
    private final static String OUTPUT_FILENAME = "src/main/resources/analyzer_output.txt";

    public static void main(String[] args) {
        GameDataReader gameReader = new GameDataReader(INPUT_FILENAME);
        GameValidator validator = new GameValidator();

        try(FileLogger fileLogger = new FileLogger(OUTPUT_FILENAME)) {
            //Read sessions
            HashMap<Long, GameSession> sessions = gameReader.getGameSessions();

            //Invalidating faulty moves
            validator.validate(sessions);

            List<Long> keys = new ArrayList<>(sessions.keySet().stream().toList());
            Collections.sort(keys);

            for (Long key : keys) {
                GameSession session = sessions.get(key);

                //looking for the first faulty move
                Optional<Action> actionOptional = session.getActions().stream().filter(a -> !a.isValid()).min(Comparator.comparingLong(Action::getTimestamp));

                //if there were no faulty moves in a session, then skip
                if (actionOptional.isEmpty())
                    continue;

                Action action = actionOptional.get();
                fileLogger.log(action.getOriginalLine());
            }
        }
    }
}