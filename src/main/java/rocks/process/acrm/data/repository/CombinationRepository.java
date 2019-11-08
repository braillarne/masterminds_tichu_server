package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Combination;
import rocks.process.acrm.data.domain.Player;

import java.util.List;

@Repository
public interface CombinationRepository extends JpaRepository<Combination, Long> {
    List<Combination> findAllByPlayer(Player p);
}
