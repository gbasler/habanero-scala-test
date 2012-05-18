package primers.helloworld

import edu.rice.habanero.HabaneroApp
import edu.rice.habanero.HabaneroHelper._

object HelloWorldApp extends HabaneroApp {

  finish {
    async {
      println("Hello World")
    }
  }

}
