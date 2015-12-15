package school.atleastonce

import akka.actor._
import akka.persistence._
import StudentActor.Msg
import school.Multiply

import scala.util.Random

object StudentActor {

  case class Msg(deliveryId: Long, multiply: Multiply)

}

class StudentActor extends ActorLogging with PersistentActor {

  def persistenceId: String = "student-id-2"

  var state = StudentState()

  def receiveCommand: Receive = {
    case Msg(deliveryId, Multiply(n1, n2)) =>
      // デリバリーIDが3の倍数の場合、1/2の確率でExceptionを発生させる
      if (deliveryId % 3 == 0 && Random.nextInt(2) == 0) {
        log.info(s" $deliveryId :  throw exception")
        Thread.sleep(2000)
        throw new Exception("fool!")
      }
      log.info(s" $deliveryId :  calculating ... ${Multiply(n1, n2)} = ${n1 * n2}")
      persist(Msg(deliveryId, Multiply(n1, n2)))(updateState)
      sender() ! TeacherActor.Confirm(deliveryId)
    case "print" => log.info(s"  result = $state")
  }

  val receiveRecover: Receive = {
    case msg: Msg =>
      log.info(s"  update state : ${msg.deliveryId}")
      updateState(msg)
  }

  def updateState(msg: Msg): Unit = {
    state = state.updated(msg)
  }
}

final case class StudentState(received: List[Long] = Nil) {
  def updated(s: Msg): StudentState = copy(received :+ s.deliveryId)

  def contains(s: Msg) = received.contains(s.deliveryId)

  override def toString = received.toString
}
