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

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author Joao Pedro Evangelista
 */
@Document
class Paper(val owner: String, val artifact: String, val contentLocation: String) {

  var id: String@Id = _

  //noinspection ComparingUnrelatedTypes

  override def equals(other: Any): Boolean = other match {
    case that: Paper =>
      (that canEqual this) &&
        id == that.id &&
        owner == that.owner &&
        artifact == that.artifact &&
        contentLocation == that.contentLocation
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Paper]

  override def hashCode(): Int = {
    val state = Seq(id, owner, artifact, contentLocation)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
