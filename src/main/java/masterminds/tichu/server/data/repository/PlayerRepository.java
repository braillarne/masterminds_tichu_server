package masterminds.tichu.server.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import masterminds.tichu.server.data.domain.Player;

import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player,Long>{
    Player findOnePlayerById(Long id);
    Player findByName(String name);
    List<Player> findAll();
}
