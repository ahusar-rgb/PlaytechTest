package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static java.lang.Character.isDigit;

public class GameDataReader {

    private final String filename;

    public GameDataReader(String filename) {
        this.filename = filename;
    }

    public HashMap<Long, GameSession> getGameSessions() {
        HashMap<Long, GameSession> sessions = new HashMap<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to open file " + filename);
        }

        try {
            //reading every line in the file
            String line = reader.readLine();
            while (line != null) {

                //Splitting the csv line
                String[] values = line.split(",");

                //Obtaining the game session
                Long gameSessionId;

                //skipping slag
                if (values.length <= 1) {
                    line = reader.readLine();
                    continue;
                }
                try {
                    gameSessionId = Long.parseLong(values[1]);
                } catch (NumberFormatException e) {
                    line = reader.readLine();
                    continue;
                }

                //creating a gameSession, if not encountered yet
                if (!sessions.containsKey(gameSessionId))
                    sessions.put(gameSessionId, new GameSession(gameSessionId, Long.parseLong(values[2])));
                GameSession session = sessions.get(gameSessionId);

                //creating and adding an action to the game session
                Action action;
                try {
                    action = getAction(line);
                } catch (InvalidDataFormatException e) {
                    //if an error occurred, the line cannot be parsed and so is invalid
                    action = new Action(line, Long.parseLong(values[0]));
                    action.invalidate();
                }
                session.addAction(action);

                //read the next line
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file " + filename);
        }

        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close file " + filename);
        }
        return sessions;
    }

    private Action getAction(String line) throws InvalidDataFormatException {
        String[] values = line.split(",");
        Long timestamp = Long.parseLong(values[0]);
        String[] actionValues = values[3].split(" ");
        Boolean byPlayer = actionValues[0].equals("P");
        ActionType actionType = ActionTypeMapper.map.get(actionValues[1]);

        if (actionType == null) {
            throw new InvalidDataFormatException("Unknown action type: " + actionValues[1]);
        }

        Action action = new Action(line, timestamp, actionType, byPlayer);

        String[] dealerCards = values[4].split("-");
        for (String cardData : dealerCards) {
            action.addDealerCard(stringToCard(cardData));
        }

        String[] playerCards = values[5].split("-");
        for (String cardData : playerCards) {
            action.addPlayerCard(stringToCard(cardData));
        }

        return action;
    }

    private Card stringToCard(String string) throws InvalidDataFormatException {
        if (string.equals("?"))
            return null;

        CardRank rank;
        if (string.length() == 2) {
            if (isDigit(string.charAt(0))) {
                rank = CardRank.values()[Integer.parseInt(string.substring(0, 1)) - 2];
            } else {
                switch (Character.toUpperCase(string.charAt(0))) {
                    case 'J' -> rank = CardRank.JACK;
                    case 'Q' -> rank = CardRank.QUEEN;
                    case 'K' -> rank = CardRank.KING;
                    case 'A' -> rank = CardRank.ACE;
                    default -> throw new InvalidDataFormatException("Unknown card value: " + string);
                }
            }
        } else if (string.length() == 3) {
            if (string.charAt(0) == '1' && string.charAt(1) == '0')
                rank = CardRank.TEN;
            else
                throw new InvalidDataFormatException("Unknown card value: " + string);
        } else {
            throw new InvalidDataFormatException("Unknown card format: " + string);
        }

        CardSuit cardSuit = switch (Character.toUpperCase(string.charAt(string.length() - 1))) {
            case 'C' -> CardSuit.CLUBS;
            case 'D' -> CardSuit.DIAMONDS;
            case 'H' -> CardSuit.HEARTS;
            case 'S' -> CardSuit.SPADES;
            default -> throw new InvalidDataFormatException("Unknown card suit: " + string);
        };

        return new Card(cardSuit, rank);
    }
}
