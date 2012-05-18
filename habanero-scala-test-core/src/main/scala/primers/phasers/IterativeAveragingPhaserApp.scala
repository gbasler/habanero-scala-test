package primers.phasers

import edu.rice.habanero.HabaneroHelper._
import edu.rice.habanero.{PhaserMode, Phaser, HabaneroApp}

object IterativeAveragingPhaserApp extends HabaneroApp {

  println("IterativeAveragingPhaserApp starting...")

  val n: Int = 10
  println("IterativeAveragingPhaserApp: creating array of size: " + (n + 1))

  val dataArray = new Array[Double](n + 2)
  println("IterativeAveragingPhaserApp: initializing array")
  for (i <- 0 to n) {
    dataArray(i) = 0
  }
  dataArray(n + 1) = 1

  println("IterativeAveragingPhaserApp: input data array = " + dataArray.deep.mkString(" "))
  finish {

    val myPhasers = new Array[Phaser](n + 2)
    for (i <- 0 to n + 1) {
      myPhasers(i) = phaser()
    }
    println("IterativeAveragingPhaserApp: Created " + (n + 2) + " phasers.")

    var ctr = 1
    while (ctr <= n) {

      val me = ctr
      val left = ctr - 1
      val right = ctr + 1

      val leftPhaser: Phaser = myPhasers(left).inMode(PhaserMode.WAIT)
      val selfPhaser: Phaser = myPhasers(me).inMode(PhaserMode.SIG)
      val rightPhaser: Phaser = myPhasers(right).inMode(PhaserMode.WAIT)
      asyncPhased(leftPhaser, selfPhaser, rightPhaser) {
        var loop = 0
        while (loop <= (40 * n)) {
          // an arbitrary limit for the loop instead of convergence test
          // first compute the value
          val loopVal = 0.5 * (dataArray(left) + dataArray(right))
          // Allow others to proceed and modify dataArray
          next
          // update the local value
          dataArray(me) = loopVal
          // notify others that value has been updated
          next

          loop += 1
        }
      }

      ctr += 1
    }
  }
  println("IterativeAveragingPhaserApp: averaged data array = " + dataArray.deep.mkString(" "))

  println("IterativeAveragingPhaserApp ends.")

}
