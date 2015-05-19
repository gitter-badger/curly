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

import akka.actor.Actor
import curly.merchant.health.HealthRoutes

/**
 * @author Joao Evangelista
 */
class SmugglerExchangeActor extends Actor with SmugglerRoutes with HealthRoutes with akka.actor.ActorLogging {

  override def receive: Receive = runRoute(simpleMerchantRoute ~ healthRoutes)

  def actorRefFactory = context
}
