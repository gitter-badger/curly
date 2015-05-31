package curly.paperclip.paper

import curly.commons.security.OwnedResource
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.springframework.data.annotation.Id

/**
 * @author Joao Pedro Evangelista
 */
@ToString
@TupleConstructor
@EqualsAndHashCode
class Paper extends OwnedResource {

    @Id
    String id

    String item
}
