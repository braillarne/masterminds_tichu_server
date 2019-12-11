package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Profile;
import rocks.process.acrm.data.domain.Result;

import java.sql.Timestamp;
import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAll();
    List<Result> findAllByProfile(Profile p);
    List<Result> findAllByDate(Timestamp time);
    List<Result> findAllByDateAndProfile(Timestamp time, Profile p);
    Result findResultById(Long id);
}
