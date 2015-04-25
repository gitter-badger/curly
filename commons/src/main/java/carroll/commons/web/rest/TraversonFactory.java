package carroll.commons.web.rest;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;

/**
 * @author João Pedro Evangelista
 * @since 09/03/15
 */
public class TraversonFactory {

    private final LoadBalancerClient loadBalancerClient;

    public TraversonFactory(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    public Traverson create(String serviceId) {
        ServiceInstance instance = loadBalancerClient.choose(serviceId);
        return new Traverson(instance.getUri(), MediaTypes.HAL_JSON);
    }
}
