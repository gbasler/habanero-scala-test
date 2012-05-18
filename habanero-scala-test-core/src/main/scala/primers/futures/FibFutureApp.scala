package primers.futures

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

/**
 * @author Shams Imam (shams@rice.edu)
 */
object FibFutureApp extends HabaneroApp {

  val N = 10
  val CUTOFF = 5

  // implicit global finish wraps a HabaneroApp
  val f = asyncFuture {
    fib(N)
  }
  val fibResult = f.get()
  println("fib(" + N + ") = " + fibResult)


  def fib(n: Int): Int = {
    if (n <= CUTOFF) {
      seqFib(n)
    } else {
      val x = asyncFuture {
        fib(n - 1)
      }
      val y = asyncFuture {
        fib(n - 2)
      }

      x.get() + y.get()
    }
  }

  def seqFib(n: Int): Int = {
    if (n < 2) {
      n
    } else {
      seqFib(n - 1) + seqFib(n - 2)
    }
  }

}
