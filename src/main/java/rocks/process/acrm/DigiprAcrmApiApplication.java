package rocks.process.acrm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rocks.process.acrm.business.service.AgentService;
import rocks.process.acrm.business.service.CustomerService;
import rocks.process.acrm.data.domain.Agent;
import rocks.process.acrm.data.domain.Customer;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DigiprAcrmApiApplication {

    @Autowired
    private AgentService agentService;

    @Autowired
    private CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(DigiprAcrmApiApplication.class, args);
    }

    @PostConstruct
    private void initDemoData() throws Exception {
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
