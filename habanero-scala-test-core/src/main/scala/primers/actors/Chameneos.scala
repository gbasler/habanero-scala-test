package primers.actors

import scala.actors.OutputChannel
import edu.rice.habanero.actor.HabaneroReactor
import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._
import ChameneosHelper._

/**
 * Inspired from: https://codereview.scala-lang.org/fisheye/browse/scala-svn/scala/branches/translucent/docs/examples/actors/chameneos-redux.scala
 */
object Chameneos extends HabaneroApp {

  val numMeetings = 1000
  val numChameneos = 10

  var mallActor: LightMall = null
  finish {
    mallActor = new LightMall(numMeetings, numChameneos)
    mallActor.start()
  }
  println("LightActorApp: num meetings = " + mallActor.sumMeetings)
}

class LightMall(var n: Int, numChameneos: Int) extends HabaneroReactor {

  val self = this
  var waitingChameneo: OutputChannel[Any] = null

  override def start(): Unit = {
    super.start()
    startChameneos()
  }

  def startChameneos(): Unit = {
    var i = 0
    while (i < numChameneos) {
      LightChameneo(this, colors(i % 3), i).start()
      i = i + 1
    }
  }

  var sumMeetings = 0
  private var numFaded = 0

  override def behavior() = {

    case msg: MeetingCountMsg => {
      numFaded = numFaded + 1
      sumMeetings = sumMeetings + msg.count
      if (numFaded == numChameneos) {
        exit()
      }
    }

    case msg: MeetMsg => {
      if (n > 0) {
        if (waitingChameneo eq null) {
          // register this as a Chameneo waiting to meet someone
          waitingChameneo = msg.sender
        } else {
          // forward the meet message to the waiting chameneo
          n = n - 1
          waitingChameneo ! msg
          waitingChameneo = null
        }
      } else {
        // no more meetings left, request chameneo to exit
        msg.sender ! ExitMsg(self)
      }
    }
  }
}

case class LightChameneo(var mall: LightMall, var color: Color, id: Int) extends HabaneroReactor {
  var meetings = 0
  var selfActor = this

  override def start(): Unit = {
    super.start()
    // go to the meeting point
    mall ! MeetMsg(color, selfActor)
  }

  override def behavior() = {

    case MeetMsg(otherColor, sender) =>
      // change my color
      color = complement(color, otherColor)
      meetings = meetings + 1
      // report new color to other chameneo
      sender ! ChangeMsg(color, selfActor)
      // go back to meeting place
      mall ! MeetMsg(color, selfActor)

    case ChangeMsg(newColor, sender) =>
      // update my color
      color = newColor
      meetings = meetings + 1
      // go back to meeting place
      mall ! MeetMsg(color, selfActor)

    case ExitMsg(sender) =>
      // fade, notify meeting count and exit :)
      color = FADED
      sender ! MeetingCountMsg(meetings, selfActor)
      exit()
  }

  override def toString() = id + "(" + color + ")"
}

object ChameneosHelper {

  abstract class Color

  case object RED extends Color

  case object YELLOW extends Color

  case object BLUE extends Color

  case object FADED extends Color

  val colors = Array[Color](BLUE, RED, YELLOW)

  abstract class Message

  case class MeetMsg(color: Color, sender: OutputChannel[Any]) extends Message

  case class ChangeMsg(color: Color, sender: OutputChannel[Any]) extends Message

  case class MeetingCountMsg(count: Int, sender: OutputChannel[Any]) extends Message

  case class ExitMsg(sender: OutputChannel[Any]) extends Message


  def complement(color: Color, otherColor: Color): Color = {
    color match {
      case RED => otherColor match {
        case RED => RED
        case YELLOW => BLUE
        case BLUE => YELLOW
        case FADED => FADED
      }
      case YELLOW => otherColor match {
        case RED => BLUE
        case YELLOW => YELLOW
        case BLUE => RED
        case FADED => FADED
      }
      case BLUE => otherColor match {
        case RED => YELLOW
        case YELLOW => RED
        case BLUE => BLUE
        case FADED => FADED
      }
      case FADED => FADED
    }
  }

}
