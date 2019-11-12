package rocks.process.acrm.business.service;

import org.junit.jupiter.api.Test;
import rocks.process.acrm.data.domain.Card;
import rocks.process.acrm.data.domain.Combination;
import rocks.process.acrm.data.domain.Suit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameBusinessServiceTest {
    GameBusinessService gameBusinessService = new GameBusinessService();

    @Test
    void evaluatePlayerPlayableCombinations() {
        List<Card> hand = new ArrayList<>();

        Card card1 = new Card(2, Suit.DOG);
        Card card2 = new Card(3, Suit.DOG);
        Card card3 = new Card(2, Suit.DRAGON);
        Card card4 = new Card(3, Suit.DRAGON);
        Card card5 = new Card(4, Suit.DOG);

        hand.add(card1);
        hand.add(card2);
        hand.add(card3);
        hand.add(card4);
        hand.add(card5);

        List<Combination> combinationList = gameBusinessService.addPossiblePairsToPlayableCombinations(hand);
        assertEquals(2,combinationList.size());

        combinationList = gameBusinessService.addPossibleRunningPairs(combinationList);
        assertEquals(1,combinationList.size());
    }
}