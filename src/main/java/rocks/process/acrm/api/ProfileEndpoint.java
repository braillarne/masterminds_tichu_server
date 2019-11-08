/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.process.acrm.business.service.CustomerService;
import rocks.process.acrm.business.service.ProfileService;
import rocks.process.acrm.data.domain.Customer;
import rocks.process.acrm.data.domain.Profile;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

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
}
