package primers.asyncs

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

object InterleavedAsync extends HabaneroApp {

  private val random = new scala.util.Random()

  def asyncProcessing(prefix: String): Unit = {
    async {
      var i = 0
      while (i < 10) {
        println(prefix + i)
        i += 1

        // busy waiting
        val limit = random.nextInt(1000)
        var r = 0
        while (r < limit) {
          r += 1
        }

      }
    }
  }

  println("InterleavedAsync starting...")
  finish {
    asyncProcessing("async 1-")
    asyncProcessing("async 2-")
  }
  println("InterleavedAsync ending.")
}
