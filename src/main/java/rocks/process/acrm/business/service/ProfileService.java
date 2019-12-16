package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.Profile;
import rocks.process.acrm.data.repository.ProfileRepository;

import java.util.Random;


/**
 * Author(S): Nelson Braillard
 */
@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // Strength set as 12

    public Profile findOneProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }

    public void saveProfile(Profile profile) throws Exception {
        try {

            if(profile.isGuest()){
                profileRepository.save(profile);
                profile.setUsername("Guest"+profile.getId().toString());

                profile.setAvatar(new Random().nextInt(14));

                profileRepository.save(profile);
            } else {
                if(profileRepository.findByUsername(profile.getUsername())!=null) {
                    throw new Exception("Username not free");
                } else {
                    String encodedPassword = encoder.encode(profile.getPassword());

                    profile.setPassword(encodedPassword);
                    profileRepository.save(profile);
                }

            }

        } catch (Exception e){
            throw new Exception("Invalid Profile information.");
        }

    }

    public Profile updateProfile(Profile profile) throws Exception {
        Profile updatedProfile = null;
        try {
            updatedProfile = profileRepository.save(profile);
        } catch (Exception e) {
            throw new Exception("Impossible to update the profile. Please check information again.");
        }
        return updatedProfile;
    }

    public Profile loginWithUsernameAndPassword(String username, String password) throws Exception {
        try {
            Profile profile = findOneProfileByUsername(username);
            if(profile.isGuest()){
                return profile;

            } else if (encoder.matches(password, profile.getPassword())) {
                return profile;

            } else {
                throw new Exception("Invalid username or password.");
            }

        } catch (Exception e) {
            throw new Exception("Invalid username or password.");
        }
    }
}
