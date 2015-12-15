package school.exactlyonce

import akka.actor._
import akka.persistence.AtLeastOnceDelivery.UnconfirmedWarning
import akka.persistence._
import TeacherActor.{Confirm, CreateExercise, Exercise}
import school.Multiply

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

object TeacherActor {

  case object CreateExercise

  case class Exercise(exerciseId: Long, multiply: Multiply)

  case class Confirm(deliveryId: Long)

}

class TeacherActor(destination: ActorSelection) extends ActorLogging with AtLeastOnceDelivery {

  def persistenceId: String = "teacher-id-1"

  var state = TeacherState()
  var exerciseId = 0
  val student = destination

  implicit val ec = context.dispatcher
  val scheduler = context.system.scheduler.schedule(0 milliseconds, 10 seconds, self, CreateExercise)

  def receiveCommand: Receive = {
    case CreateExercise =>
      exerciseId += 1
      self ! Exercise(exerciseId, Multiply(Random.nextInt(9) + 1, Random.nextInt(9) + 1))
    case Exercise(exerciseId, multiply) if (exerciseId > 5) =>
      scheduler.cancel()
      student ! "print"
    case Exercise(_, multiply) =>
      persist(MsgSent(multiply))(updateState)
    case Confirm(deliveryId) =>
      // デリバリーIDが偶数の場合、1/2の確率で確認メッセージを受信できない
      if (deliveryId % 2 == 0 && Random.nextInt(2) == 0) {
        log.info(s" $deliveryId :   lost confirmation message")
      } else {
        log.info(s" $deliveryId :   received")
        persist(MsgConfirmed(deliveryId))(updateState)
      }
    case UnconfirmedWarning(metadata) =>
      log.warning(s"  unconfirmedWarning[$metadata]")
  }

  val receiveRecover: Receive = Actor.emptyBehavior

  def updateState(evt: Evt): Unit = evt match {
    case sent: MsgSent =>
      deliver(student) { deliveryId =>
        log.info(s" $deliveryId : ${sent.multiply} = ?")
        state = state.updated(sent)
        StudentActor.Msg(deliveryId, sent.multiply)
      }
    case confirm: MsgConfirmed => confirmDelivery(confirm.deliveryId)
  }
}

final case class TeacherState(received: List[Evt] = Nil) {
  def updated(s: Evt): TeacherState = copy(received :+ s)
}

sealed trait Evt

case class MsgSent(multiply: Multiply) extends Evt

case class MsgConfirmed(deliveryId: Long) extends Evt