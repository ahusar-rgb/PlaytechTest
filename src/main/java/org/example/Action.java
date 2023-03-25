package org.example;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private final Long timestamp;

    private final String originalLine;
    private final List<Card> dealerCards = new ArrayList<>();
    private final List<Card> playerCards = new ArrayList<>();
    private ActionType actionType;
    private Boolean byPlayer;

    private Boolean isValid = true;

    public Action(String originalLine, Long timestamp, ActionType actionType, Boolean byPlayer) {
        this.originalLine = originalLine;
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.byPlayer = byPlayer;
    }

    public Action(String originalLine, Long timestamp) {
        this.originalLine = originalLine;
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public List<Card> getDealerCards() {
        return dealerCards;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Boolean isByPlayer() {
        return byPlayer;
    }

    public void addDealerCard(Card card) {
        dealerCards.add(card);
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public void addPlayerCard(Card card) {
        playerCards.add(card);
    }

    public Boolean isValid() {
        return isValid;
    }

    public void invalidate() {
        isValid = false;
    }

    public String getOriginalLine() {
        return originalLine;
    }

    public Integer getDealerCardSum() {
        return getCardSum(dealerCards);
    }

    public Integer getPlayerCardSum() {
        return getCardSum(playerCards);
    }

    private Integer getCardSum(List<Card> cards) {
        return cards.stream()
                .reduce(0,
                        (subtotal, card) -> subtotal + (card == null ? 0 : card.getValue()),
                        Integer::sum);
    }
}
