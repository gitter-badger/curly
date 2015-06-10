package curly.paperclip.paper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * @author João Evangelista
 */
@Service
class PaperService {

    private final PaperRepository repository

    @Autowired
    PaperService(PaperRepository repository) {
        this.repository = repository
    }

    @Cacheable(value = ["paper"], key = "#item")
    Paper findByItem(String item) {
        this.repository.findByItem(item)
    }

    @Cacheable(value = ["paper"], key = "#item")
    Paper findByItemAndOwner(String item, String owner) {
        this.repository.findByItemAndOwner(item, owner)
    }


    @CacheEvict(value = ["paper"], key = "#paper.item")
    void delete(Paper paper) {
        this.repository.delete(paper)
    }

    @CachePut(value = ["paper"], key = "#paper.item")
    Paper save(Paper paper) {
        this.repository.save(paper)
    }

}
