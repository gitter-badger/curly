package curly.paperclip.paper

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * @author Joao Pedro Evangelista
 */
@RepositoryRestResource(exported = false)
interface PaperRepository extends MongoRepository<Paper, String> {

    Paper findByItem(String item)

    Paper findByItemAndOwner(String item, String owner)


}
