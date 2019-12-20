package masterminds.tichu.server;

import masterminds.tichu.server.business.service.PlayerService;
import masterminds.tichu.server.business.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import masterminds.tichu.server.data.domain.Player;
import masterminds.tichu.server.data.domain.Profile;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MastermindsTichuServerApplication {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ProfileService profileService;

    public static void main(String[] args) {
        SpringApplication.run(MastermindsTichuServerApplication.class, args);
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
