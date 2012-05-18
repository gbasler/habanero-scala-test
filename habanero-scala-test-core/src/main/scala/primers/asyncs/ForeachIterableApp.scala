package primers.asyncs

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

object ForeachIterableApp extends HabaneroApp {

  println("ForeachIterableApp starting...")
  // forall has implicit finish wrapping each iteration
  1 to 20 asyncForeach {
    i =>
      val limit = scala.util.Random.nextInt(1000)
      var k = 0
      while (k < limit) {
        k += 1
      }

      println(" i = " + i)
  }
  println("ForeachIterableApp ending.")

}
