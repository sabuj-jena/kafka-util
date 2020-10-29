//sabuj.jena@logixal.com

package kafkautil.logixal.com

//rating registry actor

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import kafkautil.logixal.com.Producer._

import com.typesafe.config._

//product rating case classes
final case class ProductRatingAvg(productId: String, averageRating: Double, noOfUsers: Int)
final case class Rating(rating: Int, noOfUsers: Int)
final case class ProductRatings(productId: String, ratings: List[Rating] )

object RatingRegistry {

  //import json-formats
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._
  import spray.json._

  //actor protocol
  sealed trait Command
  final case class CreateProductRatingAvg(pra: ProductRatingAvg, replyTo: ActorRef[ActionPerformed]) extends Command
  final case class CreateProductRatings(pr: ProductRatings, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class ActionPerformed(desc : String)

  def apply(): Behavior[Command] = registry()

  val conf = ConfigFactory.load()
  private def registry(): Behavior[Command] =
    Behaviors.receiveMessage {
      case CreateProductRatingAvg(pra, replyTo) =>
        replyTo ! ActionPerformed(s"Product Rating Average for product with product id ${pra.productId} is going to be published")
        publishToKafka(conf.getString("my-app.topics.average-product-rating"), pra.productId, pra.toJson.prettyPrint)
        Behaviors.same

      case CreateProductRatings(pr, replyTo) =>
        replyTo ! ActionPerformed(s"Product Ratings for product with product is ${pr.productId} is going to be pushed into kafka ")
        publishToKafka(conf.getString("my-app.topics.product-ratings"), pr.productId, pr.toJson.prettyPrint)
        Behaviors.same
    }

}
