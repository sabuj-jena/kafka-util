//sabuj.jena@logixal.com

package kafkautil.logixal.com

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import kafkautil.logixal.com.RatingRegistry._

import scala.concurrent.Future
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout

//rating-routes-class
class RatingRoutes(ratingRegistry: ActorRef[RatingRegistry.Command])(implicit val system: ActorSystem[_]) {

  //import json-formats
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._

  //If ask takes more time than this to complete, the request is failed
  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def createProductRatingAvg(pra: ProductRatingAvg): Future[ActionPerformed] =
    ratingRegistry.ask(CreateProductRatingAvg(pra, _))

  def createProductRatings(pr: ProductRatings): Future[ActionPerformed] =
    ratingRegistry.ask(CreateProductRatings(pr, _))

  //all-routes
  val ratingRoutes: Route =
    concat(
      post {
        path("productRatingAverage") {
          entity(as[ProductRatingAvg]){ pra =>
            onSuccess(createProductRatingAvg(pra)) { perfprmed =>
              complete((StatusCodes.Created, perfprmed))
            }

          }
        }

      },
      post {
        path("productRatings") {
          entity(as[ProductRatings]){ pr =>
            onSuccess(createProductRatings(pr)) { perfprmed =>
              complete((StatusCodes.Created, perfprmed))
            }

          }
        }

      }
    )
}
