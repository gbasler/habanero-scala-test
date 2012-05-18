package primers.asyncs

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

/**
 * Async/Finish program for
 * http://2.bp.blogspot.com/_P3ewsGQzHn0/TSiPQwaWLlI/AAAAAAAAAY8/yXtDC4XHXtc/s640/ForkJoin.png
 * from article at
 * http://nurkiewicz.blogspot.com/2011/01/activiti-processes-and-executions.html
 */
object AsyncFinishPrimer extends HabaneroApp {

  println("Task-O")
  finish {
    async {
      println("Task-A")
    }
    async {
      println("Task-B")
    }
    finish {
      async {
        println("Task-B1")
      }
      async {
        println("Task-B2")
      }
    } // join B1 and B2
  } // join A and B
  println("Task-C")
}
