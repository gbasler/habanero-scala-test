package primers.asyncs

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

object ForallApp extends HabaneroApp {

  println("ForallApp starting...")
  // forall has implicit finish wrapping each iteration
  forall(1, 20) {
    i =>
      val limit = scala.util.Random.nextInt(1000)
      var k = 0
      while (k < limit) {
        k += 1
      }

      println(" i = " + i)
  }
  println("ForallApp ending.")

}
