package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class GameValidator {

    public void validate(HashMap<Long, GameSession> sessions) {
        for (GameSession gameSession : sessions.values()) {
            for (Action action : gameSession.getActions()) {
                validateAction(action);
            }
        }
    }

    private void validateAction(Action action) {
        ValidatorChain validatorChain = new ValidatorChain();
        if (action.isValid()) {

            validatorChain.addValidator(getBaseValidator());
            if (action.getActionType() != ActionType.WIN && action.getActionType() != ActionType.LOSE)
                validatorChain.addValidator(getNotFinalActionValidator());

            ActionValidator specificValidator;
            switch (action.getActionType()) {
                case JOIN -> specificValidator = getJoinValidator();
                case LEAVE -> specificValidator = getLeaveValidator();
                case HIT -> specificValidator = getHitValidator();
                case STAND -> specificValidator = getStandValidator();
                case REDEAL -> specificValidator = getRedealValidator();
                case WIN -> specificValidator = getWinValidator();
                case SHOW -> specificValidator = getShowValidator();
                case LOSE -> specificValidator = getLoseValidator();
                default -> throw new IllegalStateException("Unexpected value: " + action.getActionType());
            }

            if (specificValidator != null)
                validatorChain.addValidator(specificValidator);

            if (!validatorChain.isActionValid(action))
                action.invalidate();
        }
    }

    private ActionValidator getBaseValidator() {
        return action -> {
            List<Card> allCards = Stream.concat(action.getDealerCards().stream(), action.getPlayerCards().stream()).toList();
            if (allCards.size() != allCards.stream().distinct().toList().size())
                return false;

            if (action.getPlayerCards().contains(null))
                return false;

            return true;
        };
    }

    private ActionValidator getNotFinalActionValidator() {
        return action -> !(action.getDealerCardSum() > 21 || action.getPlayerCardSum() > 21);
    }


    private ActionValidator getJoinValidator() {
        return action -> {
            if (action.getDealerCards().size() == 2 && action.getPlayerCards().size() == 2 && action.isByPlayer()) {
                return action.getDealerCards().get(1) == null;
            }
            return false;
        };
    }

    private ActionValidator getLeaveValidator() {
        return action ->
                action.getDealerCards().size() == 0 &&
                        action.getPlayerCards().size() == 0 &&
                        action.isByPlayer();
    }

    private ActionValidator getStandValidator() {
        return action -> true;
    }

    private ActionValidator getRedealValidator() {
        return isByDealerValidator().and(getStartingActionValidator());
    }

    private ActionValidator getShowValidator() {

        return action -> true;
    }

    private ActionValidator getHitValidator() {
        return action -> {
            if (action.isByPlayer())
                return action.getPlayerCardSum() < 20;
            else
                return action.getDealerCardSum() < 17;
        };
    }

    private ActionValidator getWinValidator() {

        ActionValidator validator = action ->
                action.getDealerCardSum() <= action.getPlayerCardSum() &&
                        action.getDealerCardSum() >= 17 && action.getPlayerCardSum() <= 21;
        return isByPlayerValidator().and(validator);
    }

    private ActionValidator getLoseValidator() {

        ActionValidator validator = action ->
                action.getDealerCardSum() > action.getPlayerCardSum() ||
                        action.getPlayerCardSum() > 21;
        return isByPlayerValidator().and(validator);
    }

    private ActionValidator getStartingActionValidator() {
        return action -> {
            if (action.getDealerCards().size() == 2 && action.getPlayerCards().size() == 2) {
                return action.getDealerCards().get(1) == null;
            }
            return false;
        };
    }

    private ActionValidator isByPlayerValidator() {
        return Action::isByPlayer;
    }

    private ActionValidator isByDealerValidator() {
        return action -> !action.isByPlayer();
    }
}