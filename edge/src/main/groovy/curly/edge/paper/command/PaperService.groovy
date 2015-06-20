package curly.edge.paper.command

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import curly.commons.logging.annotation.Loggable
import curly.edge.paper.domain.Paper
import curly.edge.paper.repository.PaperClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import rx.Observable

/**
 * @author Joao Pedro Evangelista
 */
@Service
class PaperService {

    private static final Logger logger = LoggerFactory.getLogger(PaperService)

    @Autowired
    PaperClient client

    @Retryable
    @Loggable
    @HystrixCommand(fallbackMethod = "getPaperFallback")
    Observable<Optional<Paper>> getPaper(String item) {
        new ObservableResult<Optional<Paper>>() {
            @Override
            Optional<Paper> invoke() {
                def entity = client.findByItem(item)
                if (entity.statusCode.is2xxSuccessful()) {
                    logger.info "Client request returned 2xx proceeding with optional..."
                    return Optional.ofNullable(entity.body)
                } else {
                    logger.info "Client failed to request successfully returning empty!"
                    Optional.empty()
                }
            }
        }
    }

    @SuppressWarnings(["GrMethodMayBeStatic", "GroovyUnusedDeclaration"])
    Optional<Paper> getPaperFallback(String item) {
        Optional.of(new Paper("", item, ""))
    }
}
