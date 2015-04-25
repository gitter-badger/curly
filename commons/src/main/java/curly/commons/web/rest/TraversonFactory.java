/*
 * Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package curly.commons.web.rest;

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
