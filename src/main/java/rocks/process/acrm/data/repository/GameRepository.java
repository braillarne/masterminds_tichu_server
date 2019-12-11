package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Game;
import rocks.process.acrm.data.domain.State;

import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAll();
    List<Game> findAllByName(String name);
    List<Game> findAllByState(State state);
    Game findByGameId(Long id);
}
