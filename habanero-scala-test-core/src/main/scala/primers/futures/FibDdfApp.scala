package primers.futures

import edu.rice.habanero.HabaneroHelper._
import edu.rice.habanero.{DataDrivenFuture, HabaneroApp}

/**
 * @author Shams Imam (shams@rice.edu)
 */
object FibDdfApp extends HabaneroApp {

  val N = 10
  val CUTOFF = 5

  // implicit global finish wraps a HabaneroApp
  val res = ddf[Int]()
  async {
    fib(N, res)
  }
  asyncAwait(res) {
    val fibResult = res.get()
    println("fib(" + N + ") = " + fibResult)
  }


  def fib(n: Int, v: DataDrivenFuture[Int]): Unit = {
    if (n <= CUTOFF) {
      v.put(seqFib(n))
    } else {
      val res1 = ddf[Int]()
      val res2 = ddf[Int]()
      asyncAwait(res1, res2) {
        v.put(res1.get() + res2.get())
      }
      async {
        fib(n - 1, res1)
      }
      async {
        fib(n - 2, res2)
      }
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
