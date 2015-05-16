package curly

import akka.actor.Actor

/**
 * @author Joao Evangelista
 */
class MerchantExchangeActor extends Actor with MerchantRoutes with akka.actor.ActorLogging {

  override def receive: Receive = runRoute(simpleMerchantRoute)

  def actorRefFactory = context
}
