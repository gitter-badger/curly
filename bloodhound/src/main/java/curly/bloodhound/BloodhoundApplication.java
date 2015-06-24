package curly.bloodhound;

import curly.commons.config.context.EnableWorkQueueExecutor;
import curly.commons.logging.annotation.config.EnableLoggable;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableLoggable
@EnableWorkQueueExecutor
@SpringCloudApplication
@EnableElasticsearchRepositories
public class BloodhoundApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloodhoundApplication.class, args);

	}
}


//todo 2nd level cache para elasticsearch poder falhar, evcache?
//todo Artifact


