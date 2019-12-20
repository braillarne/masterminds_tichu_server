package masterminds.tichu.server.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import masterminds.tichu.server.data.domain.Card;
import masterminds.tichu.server.data.domain.Player;

import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByPlayerAssociatedToHand(Player p);
    List<Card> findAllByPlayerAssociatedToWon(Player p);
}
