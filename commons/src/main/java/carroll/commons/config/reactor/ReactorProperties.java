package carroll.commons.config.reactor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Joao on 02/04/2015.
 */
@Data
@ConfigurationProperties("reactor")
public class ReactorProperties {

    private String dispatcher;

}
