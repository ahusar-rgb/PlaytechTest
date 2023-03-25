package org.example;

import java.util.Objects;

public class Card {
    private final CardSuit suit;
    private final CardRank rank;

    public Card(CardSuit suit, CardRank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardRank getRank() {
        return rank;
    }

    public Integer getValue() {
        return rank.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getSuit() == card.getSuit() && getRank() == card.getRank();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSuit(), getRank());
    }
}
