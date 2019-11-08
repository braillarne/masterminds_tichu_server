package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.Setting;
import rocks.process.acrm.data.repository.SettingRepository;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public void saveSetting(Setting setting) throws Exception {
        settingRepository.save(setting);
    }
}
