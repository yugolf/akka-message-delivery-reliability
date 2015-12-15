package school.akkapersistence

import akka.actor._
import akka.persistence._
import StudentActor.Msg
import school.Multiply

object StudentActor {

  case class Msg(deliveryId: Long, multiply: Multiply)

}

class StudentActor extends ActorLogging with PersistentActor {

  def persistenceId: String = "student-id-2"

  var state = StudentState()

  def receiveCommand: Receive = {
    case Msg(deliveryId, Multiply(n1, n2)) =>
      log.info(s" $deliveryId :  calculating ... ${Multiply(n1, n2)} = ${n1 * n2}")
      persist(Msg(deliveryId, Multiply(n1, n2)))(updateState)
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
