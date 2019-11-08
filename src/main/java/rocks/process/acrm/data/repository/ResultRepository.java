package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Result;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAll();
    List<Result> findAllByProfile();
    List<Result> findAllByDate();
    List<Result> findAllByDateAndProfile();
    Result findById();
}
