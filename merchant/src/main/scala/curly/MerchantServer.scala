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
package curly

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

/**
 * @author Joao Evangelista
 */
object MerchantServer {
  def main(args: Array[String]) {
    implicit val system = ActorSystem()

    val service = system.actorOf(Props[MerchantExchangeActor], "merchantExchange")

    IO(Http) ! Http.Bind(service, interface = "localhost", port = 8888)
  }

}
