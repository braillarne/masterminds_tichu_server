package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Profile;

import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findAll();
    List<Profile> findAllByFirstname(String firstname);
    List<Profile> findAllByLastname(String lastname);
    Profile findProfileById(Long id);
    Profile findByUsername(String username);
}
