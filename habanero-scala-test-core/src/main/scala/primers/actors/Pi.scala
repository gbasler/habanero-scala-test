package primers.actors

import edu.rice.habanero.actor.HabaneroReactor
import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

/**
 * Computation of Pi inspired from the akka tutorial at:
 * http://akka.io/docs/akka/snapshot/intro/getting-started-first-scala.html
 *
 *
 * @author Shams Imam (shams@rice.edu)
 */

object Pi extends HabaneroApp {

  finish {
    calculate(nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000)
  }

  sealed trait PiMessage

  case object Calculate extends PiMessage

  case class Work(start: Int, nrOfElements: Int, sender: HabaneroReactor) extends PiMessage

  case class Result(value: Double) extends PiMessage

  case class Exit() extends PiMessage

  case class PiApproximation(pi: Double)

  class Worker extends HabaneroReactor {

    override def exit() {
      super.exit()
    }

    def calculatePiFor(start: Int, nrOfElements: Int): Double = {
      var acc = 0.0
      for (i ← start until (start + nrOfElements))
        acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
      acc
    }

    override def behavior() = {
      case Work(start, nrOfElements, sender) ⇒
        // perform the work
        val result: Double = calculatePiFor(start, nrOfElements)
        sender ! Result(result)
      case Exit() =>
        exit()
    }
  }

  class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: HabaneroReactor)
      extends HabaneroReactor {

    private final val selfRef = this
    var pi: Double = _
    var nrOfResults: Int = _

    val workers = Array.tabulate[Worker](nrOfWorkers) {
      i =>
        val worker: Worker = new Worker
        worker.start()
        worker
    }

    override def behavior() = {
      case Calculate ⇒
        for (i ← 0 until nrOfMessages) {
          workers(i % nrOfWorkers) ! Work(i * nrOfElements, nrOfElements, selfRef)
        }
      case Result(value) ⇒
        pi += value
        nrOfResults += 1
        if (nrOfResults == nrOfMessages) {
          for (i ← 0 until nrOfWorkers) {
            workers(i).exit()
          }
          // Send the result to the listener
          listener ! PiApproximation(pi)
          // Stops this actor
          exit()
        }
    }

  }

  class Listener extends HabaneroReactor {

    private final val startTime = System.nanoTime()

    override def behavior() = {
      case PiApproximation(pi) ⇒
        val endTime = System.nanoTime()
        val duration = ((endTime - startTime) / 1e6).asInstanceOf[Int]
        println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t%s ms"
            .format(pi, duration))
        exit()
    }
  }


  def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) {

    // create the result listener, which will print the result
    val listener = new Listener
    listener.start()

    // create the master
    val master = new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener)
    master.start()

    // start the calculation
    master ! Calculate

  }
}
