package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.Profile;
import rocks.process.acrm.data.repository.ProfileRepository;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Profile findOneProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }

    public void saveProfile(Profile profile) throws Exception {
        try {
            profileRepository.save(profile);
        } catch (Exception e){
            throw new Exception("Invalid Profile information.");
        }

    }

    public Profile loginWithUsernameAndPassword(String username, String password) throws Exception {
        try {
            Profile profile = findOneProfileByUsername(username);

            if (profile.getPassword().equals(password)) {
                return profile;
            } else {
                throw new Exception("Invalid username or password.");
            }

        } catch (Exception e) {
            throw new Exception("Invalid username or password.");
        }
    }
}
