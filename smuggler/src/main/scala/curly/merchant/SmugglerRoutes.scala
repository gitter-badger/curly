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
package curly.merchant

import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http.{MediaTypes, _}
import spray.routing.HttpService

import scala.concurrent.Future

/**
 * @author Joao Evangelista
 */

trait SmugglerRoutes extends HttpService {


  val simpleMerchantRoute = {
    path("exchange" / Segment / Segment) { (user, repo) =>
      get {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            implicit val system = ActorSystem()
            import system.dispatcher
            val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
            pipeline(Get(s"https://api.github.com/repos/$user/$repo") ~>
              addHeader("Accept", "application/vnd.github.v3+json"))
          }
        }
      }
    } ~ path("exchange" / Segment / Segment / "readme") { (user, repo) =>
      respondWithMediaType(MediaTypes.`application/json`) {
        complete {
          implicit val system = ActorSystem()
          import system.dispatcher
          val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
          pipeline(Get(s"https://api.github.com/repos/$user/$repo/readme") ~>
            addHeader("Accept", "application/vnd.github.v3.raw"))
        }
      }

    }
  }


}
