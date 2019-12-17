package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.process.acrm.business.service.ProfileService;
import rocks.process.acrm.data.domain.Profile;

import javax.validation.ConstraintViolationException;
import java.net.URI;

/**
 * Author(s): Nelson Braillard
 */
@RestController
@RequestMapping(path = "/api")
public class ProfileEndpoint {
    @Autowired
    private ProfileService profileService;

    @GetMapping(path = "/profile/{username}/{password}", produces = "application/json")
    public ResponseEntity<Profile> getProfile(
        @PathVariable(value = "username") String username,
        @PathVariable(value = "password") String password) {

        Profile profile = null;
        try {
            profile = profileService.loginWithUsernameAndPassword(username, password);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(profile);
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Profile> login(@RequestBody Profile profile) {
        try {
            profile = profileService.loginWithUsernameAndPassword(profile.getUsername(), profile.getPassword());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }
        return ResponseEntity.ok(profile);
    }

    @PostMapping(path = "/profile", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Profile> postProfile(@RequestBody Profile profile) throws Exception {
        try {
            profileService.saveProfile(profile);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{customerId}")
            .buildAndExpand(profile.getId()).toUri();

        return ResponseEntity.created(location).body(profile);
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
    public ResponseEntity<Profile> postProfile() throws Exception {
        Profile profile = new Profile();
        profile.setGuest(true);

        try {
            profileService.saveProfile(profile);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{customerId}")
            .buildAndExpand(profile.getId()).toUri();

        return ResponseEntity.created(location).body(profile);
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
