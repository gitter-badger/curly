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

import java.nio.file.{Path, Paths}

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import curly.commons.github.OctoUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.Retryable
import rx.Observable

/**
 * I/O Commands for data
 * for check ones create a new command
 *
 * @author Joao Pedro Evangelista
 */
class PaperHystrixCommand @Autowired()(val repository: PaperRepository,
                                       val storageAccessor: StorageAccessor)
  extends PaperCommand {

  @Retryable
  @HystrixCommand(fallbackMethod = "fallbackGetPaperByArtifact")
  override def getPaperByArtifact(artifact: String): Observable[Option[RawPaper]] = {
    new ObservableResult[Option[RawPaper]] {
      override def invoke(): Option[RawPaper] = {
        val paper: = Option(repository.findByArtifact(artifact))
        readFromLocation(paper)
      }
    }
  }

  @Retryable
  @HystrixCommand(fallbackMethod = "fallbackWriteContent")
  override def writeContent(content: String, artifact: String): Observable[Option[Path]] = {
    new ObservableResult[Option[Path]] {
      override def invoke(): Option[Path] = {
        Option(storageAccessor.write(content, artifact)) match {
          case Some(s) => Some(Paths.get(s))
          case _ => None
        }
      }
    }
  }

  @Retryable
  @HystrixCommand
  override def savePaper(path: Path, artifact: String, octoUser: OctoUser): Observable[Option[Paper]] = {
    new ObservableResult[Option[Paper]] {
      override def invoke(): Option[Paper] = {
        Option(repository.save(new Paper(octoUser.getId.toString, artifact, path.toAbsolutePath.toString)))
      }
    }
  }

  @HystrixCommand
  override def getPaperForOwner(artifact: String, owner: Option[OctoUser]) = {
    new ObservableResult[Option[RawPaper]] {
      override def invoke(): Option[RawPaper] = {
        owner match {
          case Some(o) =>
            val paper = Option(repository.findByArtifactAndOwner(artifact, o.getId.toString))
            readFromLocation(paper)
          case _ => None
        }
      }
    }
  }

  private def readFromLocation(paper: Option[Paper]): Option[RawPaper] = {
    paper match {
      case Some(mPaper) => Option(storageAccessor.rawContent(mPaper.contentLocation)) match {
        case
          Some(mContent) => Some(new RawPaper(mPaper.owner, mPaper.artifact, mContent))
        case _ => None
      }
      case _ => None
    }
  }

  @HystrixCommand
  override def deletePaper(paperId: String, octoUser: OctoUser) = {
    repository.deleteByIdAndOwner(paperId, octoUser.getId.toString)
  }

  def fallbackGetPaperByArtifact(artifact: String): Option[RawPaper] = {
    Option(new RawPaper("", artifact, ""))
  }

  def fallBackWriteContent(content: String, artifact: String): Option[Path] = {
    None
  }
}
