package curly.paperclip.paper

import curly.commons.security.OwnedResource
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author Joao Pedro Evangelista
 */
@Document
class RawPaper(val user: String, val artifact: String, val content: String) extends OwnedResource {

  override def getOwner: String = user

  override def equals(other: Any): Boolean = other match {
    case that: RawPaper =>
      (that canEqual this) &&
        user == that.user &&
        artifact == that.artifact &&
        content == that.content
    case _ => false
  }

  //noinspection ComparingUnrelatedTypes
  def canEqual(other: Any): Boolean = other.isInstanceOf[RawPaper]

  override def hashCode(): Int = {
    val state = Seq(user, artifact, content)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
