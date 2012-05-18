package primers.phasers

import edu.rice.habanero.HabaneroHelper._
import edu.rice.habanero.{PhaserMode, HabaneroApp}

object SimplePhaserApp extends HabaneroApp {

  val aPhaser = phaser() // default phaser gets PhaserMode.SIG_WAIT mode

  // activity that produces registers in SIGNAL mode
  val prodPhaser = aPhaser.inMode(PhaserMode.SIG)
  asyncPhased(prodPhaser) {
    println("Producer prints first")
    next // this will signal the consumer
  }

  // activity that consumes registers in WAIT mode
  val consPhaser = aPhaser.inMode(PhaserMode.WAIT)
  asyncPhased(consPhaser) {
    next // wait till producer signals
    println("Consumer prints second")
  }

}
