//sabuj.jena@logixal.com

package kafkautil.logixal.com

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.util.Failure
import scala.util.Success


object MainApp {
  //start-http-server
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    //akka-http still needs a classic ActorSystem to start
    import system.executionContext
    val futureBinding = Http().newServerAt("localhost", port = 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
  def main(args: Array[String]): Unit ={
    //server-bootstrapping
    val rootBehavior = Behaviors.setup[Nothing]{ context =>
      val ratingRegistryActor = context.spawn(RatingRegistry(), "RatingRegistryActor")
      context.watch(ratingRegistryActor)

      val routes = new RatingRoutes(ratingRegistryActor)(context.system)
      startHttpServer(routes.ratingRoutes)(context.system)
      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "sabujAkkaHttpServer")

  }
}
