package primers.futures

import edu.rice.habanero.HabaneroHelper._
import edu.rice.habanero.HabaneroApp

object DdfApp extends HabaneroApp {

  val aDdf = ddf[Int]()
  // awaiting asyncs are only triggered after all the ddf have a value
  asyncAwait(aDdf) {
    // ddf.get() should only be invoked when we "know" the value is available
    // we are guarateed the value is available if we execute ddf.get() inside

    println("1. after put, ddf value = " + aDdf.get())
  }
  asyncAwait(aDdf) {
    println("2. after put, ddf value = " + aDdf.get())
  }
  // this task will populate the DDF
  async {
    val ddfVal: Int = 5
    println("putting ddf = " + ddfVal)
    aDdf.put(ddfVal)
  }

}
