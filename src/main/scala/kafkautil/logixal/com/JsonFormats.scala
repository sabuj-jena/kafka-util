//sabuj.jena@logixal.com

package kafkautil.logixal.com

//json-formats
import spray.json.DefaultJsonProtocol

import kafkautil.logixal.com.RatingRegistry.ActionPerformed

object JsonFormats {
  //import the Default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val productRatingAvgJsonFormat = jsonFormat3(ProductRatingAvg)
  implicit val ratiingsJsonFormat = jsonFormat2(Rating)
  implicit val productRatingsJsonFormat = jsonFormat2(ProductRatings)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)

}
