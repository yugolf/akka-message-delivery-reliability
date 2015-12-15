package school.atmostonce

import akka.actor._
import StudentActor.Msg
import school.Multiply

object StudentActor {

  case class Msg(deliveryId: Long, multiply: Multiply)

}

class StudentActor extends ActorLogging with Actor {

  def receive: Receive = {
    case Msg(deliveryId, Multiply(n1, n2)) =>
      log.info(s" $deliveryId :  calculating ... ${Multiply(n1, n2)} = ${n1 * n2}")
  }
}