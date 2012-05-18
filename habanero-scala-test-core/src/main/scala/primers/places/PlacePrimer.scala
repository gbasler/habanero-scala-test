package primers.places

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

/**
 * Run this with multiple places to see different output, e.g.
 * hscala -Dhs.places=4:1 places.PlacePrimer
 */
object PlacePrimer extends HabaneroApp {

  println("PlacePrimer starting...")
  finish {
    // app starts out at place-0
    async {
      println("A-0. At " + here)
      async(here().next) {
        println("B-1. At " + here)
      }
      async(here()) {
        println("C-1. At " + here)
        async(here().next) {
          println("D-2. At " + here)
        }
        async(here().prev) {
          println("E-2. At " + here)
        }
      }
    }
  }
  println("PlacePrimer ending.")

}
