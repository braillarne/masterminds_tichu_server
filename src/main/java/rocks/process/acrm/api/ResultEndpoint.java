package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.process.acrm.business.service.ProfileService;
import rocks.process.acrm.business.service.ResultService;
import rocks.process.acrm.data.domain.Profile;
import rocks.process.acrm.data.domain.Result;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

/**
 * Author(s): Nelson Braillard
 */
@RestController
@RequestMapping(path = "/api")
public class ResultEndpoint {

    @Autowired
    private ResultService resultService;

    @GetMapping(path = "/result", consumes = "application/json", produces = "application/json")
    public List<Result> getAll() {
        return resultService.getAll();
    }

    @GetMapping(path = "/result/{id}", consumes = "application/json", produces = "application/json")
    public List<Result> getAllByProfileID(@PathVariable(value = "id") String id) {
        return resultService.getAllByProfile(Long.parseLong(id));
    }

}
