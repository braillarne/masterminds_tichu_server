package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rocks.process.acrm.business.service.ResultService;
import rocks.process.acrm.data.domain.Result;

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

    @GetMapping(path = "/result/ratio/{id}", consumes = "application/json", produces = "application/json")
    public String getRatioProfileID(@PathVariable(value = "id") String id) {
        return resultService.getRatioByProfile(Long.parseLong(id));
    }

}
