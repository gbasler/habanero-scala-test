package primers.places

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

/**
 * Run this with multiple places to see different output, e.g.
 * hscala -Dhs.places=4:1 places.PlacePrimer
 */
object PlacesTest extends HabaneroApp {

  println("Places starting...")
  println("Places: " + numPlaces)

  finish {
    // app starts out at place-0
    async {
      async(here().next) {
        println("A-1. At " + here)
        async(here().next) {
          println("B-1. At " + here)
          async(here().next) {
            println("C-1. At " + here)
            async(here().next) {
              println("D-1. At " + here)
            }
          }
        }
      }

      println("Main. At " + here)
    }
  }
  println("Places ending.")

}
