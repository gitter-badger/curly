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
import curly.commons.logging.annotation.Loggable
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.retry.annotation.Retryable
import rx.Observable

/**
 * I/O Commands for data
 * for check ones create a new command
 *
 * @author Joao Pedro Evangelista
 */
class PaperHystrixCommand @Autowired()(val repository: PaperRepository)
  extends PaperCommand {

  val logger = LoggerFactory.getLogger(classOf[PaperHystrixCommand])

  @Loggable
  @Retryable(maxAttempts = 2)
  @HystrixCommand
  override def getPaperByArtifact(item: String): Observable[Option[Paper]] = {
    new ObservableResult[Option[Paper]] {
      override def invoke(): Option[Paper] = {
        logger.debug("Querying for item {}", item)
        Option(repository.findByItem(item))
      }
    }
  }

  @Loggable
  @HystrixCommand
  override def deletePaper(paperId: String, octoUser: OctoUser): Unit = {
    Option(repository.findOne(paperId)) match {
      case Some(paper) =>
        logger.debug("Found a match: {}, now matching for owner equality", paper)
        if (paper.owner == octoUser.getId.toString) {
          logger.debug("User match successful: {}, deleting...", octoUser.getId)
          repository.delete(paper.id)
        } else {
          logger.warn("Intercepted attempt to delete a non owned resource")
          throw new UnsupportedOperationException
        }
      case None => throw new ResourceNotFoundException("Cannot find candidate: " + paperId + " for deletion")
    }
  }

  @HystrixCommand
  override def savePaper(paper: Paper, octoUser: OctoUser): Observable[Option[Paper]] = {
    new ObservableResult[Option[Paper]] {
      override def invoke(): Option[Paper] = {
        Option(paper.id) match {
          case Some(i) => merge(paper)
          case None => save(paper, octoUser)
        }
      }
    }
  }


  def save(paper: Paper, octoUser: OctoUser): Option[Paper] = {
    if (paper.owner == octoUser.getId.toString) Option(repository.save(paper)) else None
  }

  private def merge(paper: Paper): Option[Paper] = {
    Option(repository.findOne(paper.id)) match {
      case Some(r) =>
        if (wasModified(paper, r)) Option(repository.save(paper)) else None
      case _ => None
    }
  }

  private def wasModified(paper: Paper, original: Paper): Boolean = {
    paper.id == original.id && paper.owner == original.owner && paper.item == original.item
  }

  override def getPaperForOwner(artifact: String, owner: Option[OctoUser]): Observable[Option[Paper]] = ???
}
