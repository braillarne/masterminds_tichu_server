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

    public Profile getOne(Long id) {
        return profileRepository.findProfileById(id);
    }

    public Profile saveProfile(Profile profile) throws Exception {
        try {

            if(profile.isGuest()){
                profileRepository.save(profile);
                profile.setUsername("Guest"+profile.getId().toString());

                profile.setAvatar(new Random().nextInt(14));

                profile.setSetting("DEFAULT");

                return profileRepository.save(profile);
            } else {
                if(profileRepository.findByUsername(profile.getUsername())!=null) {
                    throw new Exception("Username not free");
                } else {
                    String encodedPassword = encoder.encode(profile.getPassword());

                    profile.setPassword(encodedPassword);
                    profile.setSetting("DEFAULT");

                    return profileRepository.save(profile);
                }

            }

        } catch (Exception e){
            throw new Exception("Invalid Profile information.");
        }

    }

    public Profile updateProfile(Profile profile) throws Exception {
        Profile updatedProfile = profileRepository.getOne(profile.getId());
        try {
            if(profile.getFirstname()!=null){
                updatedProfile.setFirstname(profile.getFirstname());
            }
            if(profile.getLastname()!=null){
                updatedProfile.setLastname(profile.getLastname());
            }
            if(profile.getUsername()!=null){
                updatedProfile.setUsername(profile.getUsername());
            }
            if(profile.getSetting()!=null){
                updatedProfile.setSetting(profile.getSetting());
            }
            if(profile.getPassword()!=null) {
                updatedProfile.setPassword(encoder.encode(profile.getPassword()));
            }
            updatedProfile = profileRepository.save(updatedProfile);
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

    public void deleteProfile(Long id) throws Exception{
        try {
            profileRepository.delete(profileRepository.getOne(id));
        } catch (Exception e) {
            throw new Exception();
        }
    }
}
