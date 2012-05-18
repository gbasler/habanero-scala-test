package primers.phasers

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

object ForallPhasedApp extends HabaneroApp {

  println("ForallIterableApp starting...")
  // forall has implicit finish wrapping each iteration
  1 to 10 asyncForall {
    i =>
      // first phase
      println("Hello from " + i)

      // barrier, forall has a phaser by default
      next

      // second phase
      println("Goodbye from " + i)
  }
  println("ForallIterableApp ending.")

}
