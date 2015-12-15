package school.akkapersistence

import akka.actor._
import com.typesafe.config.ConfigFactory

object MathApp {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty || args.head == "student" || args.head == "s") {
      println("- I'm a student")
      startStudentSystem()
    }
    if (args.isEmpty || args.head == "teacher" || args.head == "t") {
      println("- I'm a teacher")
      startTeacherSystem()
    }
  }

  def startStudentSystem(): Unit = {
    val system = ActorSystem("StudentSystem", ConfigFactory.load("student"))
    system.actorOf(Props[StudentActor], "student")

    println("- Started StudentSystem")
  }

  def startTeacherSystem(): Unit = {
    val system = ActorSystem("TeacherSystem", ConfigFactory.load("teacher"))

    val studentConfig = ConfigFactory.load("student")
    val hostname = studentConfig.getString("akka.remote.netty.tcp.hostname")
    val port = studentConfig.getString("akka.remote.netty.tcp.port")
    val student = system.actorSelection(s"akka.tcp://StudentSystem@${hostname}:${port}/user/student")
    system.actorOf(Props(new TeacherActor(student)), "teacherActor")

    println("- Started TeacherSystem")
  }
}
