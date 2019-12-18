package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Team;

/**
 * Author(S): Nelson Braillard
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> { }