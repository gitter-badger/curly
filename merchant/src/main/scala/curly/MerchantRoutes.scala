package curly

import spray.http.MediaTypes
import spray.routing.HttpService

/**
 * @author Joao Evangelista
 */
trait MerchantRoutes extends HttpService {

  val simpleMerchantRoute = {
    path("exchange" / Segment) { userLogin =>
      get {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete(s"hello $userLogin")
        }
      }
    }
  }


}
