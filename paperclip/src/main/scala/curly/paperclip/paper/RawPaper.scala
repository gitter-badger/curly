/*
 *        Copyright 2015 the original author or authors.
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
