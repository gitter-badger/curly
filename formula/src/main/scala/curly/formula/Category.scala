package curly.formula

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author João Evangelista
 */
@Document
case class Category(@Id id: String, name: String) extends Serializable
