package masterminds.tichu.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import masterminds.tichu.server.business.service.ProfileService;
import masterminds.tichu.server.data.domain.Profile;

import javax.validation.ConstraintViolationException;

/**
 * Author(s): Nelson Braillard
 */
@RestController
@RequestMapping(path = "/api")
public class ProfileEndpoint {
    @Autowired
    private ProfileService profileService;

    @GetMapping(path = "/profile/{id}", consumes = "application/json", produces = "application/json")
    public Profile getOne(@PathVariable(value = "id") String id) {
        Profile profile = null;
        try {
            profile = profileService.getOne(Long.parseLong(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return profile;
    }

    @GetMapping(path = "/profile/{username}/{password}", produces = "application/json")
    public Profile getProfile(
        @PathVariable(value = "username") String username,
        @PathVariable(value = "password") String password) {

        Profile profile = null;
        try {
            profile = profileService.loginWithUsernameAndPassword(username, password);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return profile;
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public Profile login(@RequestBody Profile profile) {
        try {
            profile = profileService.loginWithUsernameAndPassword(profile.getUsername(), profile.getPassword());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }
        return profile;
    }

    @PostMapping(path = "/profile", consumes = "application/json", produces = "application/json")
    public Profile postProfile(@RequestBody Profile profile) throws Exception {
        try {
            profileService.saveProfile(profile);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        return profile;
    }

    @PutMapping(path = "/profile", consumes = "application/json", produces = "application/json")
    public Profile updateProfile(@RequestBody Profile profile) throws Exception {
        try {
            profile = profileService.updateProfile(profile);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return profile;
    }

    @PostMapping(path = "/profile/newGuest", consumes = "application/json", produces = "application/json")
    public Profile postProfile() throws Exception {
        Profile profile = new Profile();
        Profile newProfile = null;

        profile.setGuest(true);

        try {
            newProfile = profileService.saveProfile(profile);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        return newProfile;
    }

    @DeleteMapping(path = "/profile/{id}", consumes = "application/json", produces = "application/json")
    public void deleteProfile(@PathVariable(value = "id") String id) throws Exception {
        try{
            profileService.deleteProfile(Long.parseLong(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
