package school.akkapersistence

import akka.actor._
import TeacherActor.{CreateExercise, Exercise}
import school.Multiply

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

object TeacherActor {

  case object CreateExercise

  case class Exercise(exerciseId: Long, multiply: Multiply)

}

class TeacherActor(destination: ActorSelection) extends ActorLogging with Actor {

  var exerciseId = 0
  val student = destination

  implicit val ec = context.dispatcher
  val scheduler = context.system.scheduler.schedule(0 milliseconds, 3 seconds, self, CreateExercise)

  def receive: Receive = {
    case CreateExercise =>
      exerciseId += 1
      self ! Exercise(exerciseId, Multiply(Random.nextInt(9) + 1, Random.nextInt(9) + 1))
    case Exercise(exerciseId, multiply) if (exerciseId > 5) =>
      scheduler.cancel()
      student ! "print"
    case Exercise(deliveryId, multiply) =>
      log.info(s" $exerciseId : ${multiply} = ?")
      student ! StudentActor.Msg(deliveryId, multiply)
  }
}