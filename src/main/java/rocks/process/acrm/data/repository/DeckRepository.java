package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Deck;

/**
 * Author(S): Nelson Braillard
 */
@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
    Deck findByDeckId(Long id);
}
