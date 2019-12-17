package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.Result;
import rocks.process.acrm.data.domain.Setting;
import rocks.process.acrm.data.repository.ProfileRepository;
import rocks.process.acrm.data.repository.ResultRepository;

import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public List<Result> getAll() {
        return resultRepository.findAll();
    }

    public List<Result> getAllByProfile(Long profileID) {
        return resultRepository.findAllByProfile(profileRepository.getOne(profileID));
    }
}
