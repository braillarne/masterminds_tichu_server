package rocks.process.acrm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rocks.process.acrm.business.service.AgentService;
import rocks.process.acrm.business.service.CustomerService;
import rocks.process.acrm.business.service.PlayerService;
import rocks.process.acrm.data.domain.Agent;
import rocks.process.acrm.data.domain.Customer;
import rocks.process.acrm.data.domain.Player;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DigiprAcrmApiApplication {

    @Autowired
    private AgentService agentService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PlayerService playerService;

    public static void main(String[] args) {
        SpringApplication.run(DigiprAcrmApiApplication.class, args);
    }

    @PostConstruct
    private void initDemoData() throws Exception {
        Player player = new Player();
        player.setName("Nelson");
        player.setHost(true);
        playerService.savePlayer(player);

        Agent agent = new Agent();
        agent.setName("Nelson Braillard");
        agent.setEmail("nelson@braillard.ch");
        agentService.saveAgent(agent);
        Customer customer = new Customer();
        customer.setName("Bob Thecustomer");
        customer.setMobile("+41790000000");
        customer.setEmail("bob@the-customer.ch");
        customer.setAgent(agent);
        customerService.editCustomer(customer);
    }
}
