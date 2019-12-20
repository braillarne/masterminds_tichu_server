package masterminds.tichu.server.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import masterminds.tichu.server.data.domain.Game;
import masterminds.tichu.server.data.domain.State;

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
