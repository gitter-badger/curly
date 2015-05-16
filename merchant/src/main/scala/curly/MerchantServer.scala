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
