package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Profile;
import rocks.process.acrm.data.domain.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Setting findSettingByProfile(Profile profile);
}
