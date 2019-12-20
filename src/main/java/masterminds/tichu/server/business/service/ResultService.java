package masterminds.tichu.server.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import masterminds.tichu.server.data.domain.Result;
import masterminds.tichu.server.data.repository.ProfileRepository;
import masterminds.tichu.server.data.repository.ResultRepository;

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

    public String getRatioByProfile(Long profileID) {
        return resultRepository.findAllByProfileAndIsWinner(profileRepository.getOne(profileID), true).size() + " / " + resultRepository.findAllByProfile(profileRepository.getOne(profileID)).size();
    }
}
