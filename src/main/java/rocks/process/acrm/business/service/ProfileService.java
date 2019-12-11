package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.Profile;
import rocks.process.acrm.data.repository.ProfileRepository;

/**
 * Author(S): Nelson Braillard
 */
@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Profile findOneProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }

    public void saveProfile(Profile profile) throws Exception {
        try {

            if(profile.isGuest()){
                profileRepository.save(profile);
                profile.setUsername("Guest"+profile.getId().toString());
                profileRepository.save(profile);
            } else {
                if(profileRepository.findByUsername(profile.getUsername())!=null) {
                    throw new Exception("Username not free");
                } else {
                    profileRepository.save(profile);
                }

            }

        } catch (Exception e){
            throw new Exception("Invalid Profile information.");
        }

    }

    public Profile loginWithUsernameAndPassword(String username, String password) throws Exception {
        try {
            Profile profile = findOneProfileByUsername(username);
            if(profile.isGuest()){
                return profile;
            } else if (profile.getPassword().equals(password)) {
                return profile;
            } else {
                throw new Exception("Invalid username or password.");
            }

        } catch (Exception e) {
            throw new Exception("Invalid username or password.");
        }
    }
}
