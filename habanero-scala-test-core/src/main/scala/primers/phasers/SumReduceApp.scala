package primers.phasers

import edu.rice.habanero.HabaneroHelper._
import edu.rice.habanero._

object SumReduceApp extends HabaneroApp {

  finish {
    val aPhaser: Phaser = phaser(PhaserMode.SIG_WAIT, 10)
    val sumAccum = intAccumulator(Operator.SUM, aPhaser)

    // only foreach supports registering additional phasers
    1 to 30 chunked (4) phased (aPhaser.inMode(PhaserMode.SIG)) asyncForeach {
      i =>
        sumAccum.send(i)
    }

    asyncPhased(aPhaser.inMode(PhaserMode.WAIT)) {
      // wait for the foreach tasks to complete
      next
      val resVal: Int = sumAccum.result()
      println("Sum(1..30) = " + resVal)
    }
  }
}
