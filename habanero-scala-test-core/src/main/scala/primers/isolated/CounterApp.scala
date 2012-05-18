package primers.isolated

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

object CounterApp extends HabaneroApp {

  println("CounterApp starts...")

  val r = new scala.util.Random()

  val n = 100
  val numIter = 20

  var counter: Long = 0
  finish {
    foreach(1, n) {
      i =>
        var j = 0
        while (j < numIter) {

          // Start: busy waiting
          var b: Int = 0
          val lim: Int = r.nextInt(1000)
          while (b < lim) {
            b += 1
          }
          // End: busy waiting

          // modifying counter inside isolated to avoid data races
          isolated {
            counter += 1
          }

          j += 1
        }
    }
  }

  println("Counter Value = " + counter + " [expected=" + (n * numIter) + "]")

  println("CounterApp ends.")

}
