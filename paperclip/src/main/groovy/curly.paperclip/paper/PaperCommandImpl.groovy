package curly.paperclip.paper

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import curly.commons.logging.annotation.Loggable
import org.springframework.stereotype.Service
import rx.Observable

import javax.inject.Inject
import java.lang.Override as Implements

/**
 * @author Joao Pedro Evangelista
 */
@Service
class PaperCommandImpl implements PaperCommand {

    private PaperRepository repository

    @Inject
    PaperCommandImpl(PaperRepository paperRepository) {
        this.repository = paperRepository
    }

    @Loggable
    @Implements
    @HystrixCommand
    Observable<Optional<Paper>> getByItem(String item) {
        new ObservableResult<Optional<Paper>>() {
            @Override
            Optional<Paper> invoke() {
                return Optional.ofNullable(repository.findByItem(item))
            }
        }
    }
}
