package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Card;
import rocks.process.acrm.data.domain.Combination;
import rocks.process.acrm.data.domain.Deck;
import rocks.process.acrm.data.domain.Player;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByPlayableCombinations(Combination combination);
    List<Card> findAllByPlayerAssociatedToHand(Player p);
    List<Card> findAllByPlayerAssociatedToWon(Player p);
}
