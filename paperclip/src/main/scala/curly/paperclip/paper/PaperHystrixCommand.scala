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

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import curly.commons.github.OctoUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.Retryable
import rx.Observable

/**
 * @author Joao Pedro Evangelista
 */
class PaperHystrixCommand @Autowired()(val repository: PaperRepository) extends PaperCommand {

  @Retryable
  @HystrixCommand(fallbackMethod = "fallbackGetPaperByArtifact")
  override def getPaperByArtifact(artifact: String): Observable[Option[Paper]] = {
    new ObservableResult[Option[Paper]] {
      override def invoke(): Option[Paper] = {
        Option(repository.findByArtifact(artifact))
      }
    }
  }

  @HystrixCommand
  override def getPaperForOwner(artifact: String, owner: Option[OctoUser]) = {
    new ObservableResult[Option[Paper]] {
      override def invoke(): Option[Paper] = {
        owner match {
          case Some(o) => Option(repository.findByArtifactAndOwner(artifact, o.getId.toString))
          case None => throw new IllegalArgumentException
        }
      }
    }
  }

  def fallbackGetPaperByArtifact(artifact: String): Option[Paper] = {
    Option(new Paper("", "", ""))
  }
}
