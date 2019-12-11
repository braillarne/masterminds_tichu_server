package rocks.process.acrm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rocks.process.acrm.business.service.PlayerService;
import rocks.process.acrm.business.service.ProfileService;
import rocks.process.acrm.data.domain.Player;
import rocks.process.acrm.data.domain.Profile;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DigiprAcrmApiApplication {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ProfileService profileService;

    public static void main(String[] args) {
        SpringApplication.run(DigiprAcrmApiApplication.class, args);
    }

    @PostConstruct
    private void initDemoData() throws Exception {
        Profile profile = new Profile();
        profile.setGuest(false);
        profile.setFirstname("Nelson");
        profile.setLastname("Braillard");
        profile.setPassword("1234");
        profile.setUsername("braillarne");
        profileService.saveProfile(profile);

        Player player = new Player();
        player.setName("Nelson");
        player.setHost(true);
        playerService.savePlayer(player);
    }
}
