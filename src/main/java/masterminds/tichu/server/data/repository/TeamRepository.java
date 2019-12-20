package masterminds.tichu.server.data.repository;

import masterminds.tichu.server.data.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author(S): Nelson Braillard
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> { }