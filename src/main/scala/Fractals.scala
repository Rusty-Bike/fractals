import sdl2.SDL._
import sdl2.Extras._

import scala.scalanative.native._

object Fractals extends SdlApp(c"Fractals", 800, 800) with App {
  override def main(args: Array[String]): Unit = super.main(args)

  override def onEvent(event: Ptr[SDL_Event]): Unit = {
    event.type_ match {
      case SDL_QUIT =>
        SDL_Quit()
        System.exit(0)
      case _ =>
        ()
    }
  }

  override def onDraw(): Unit = {}

  override def onIdle(): Unit = {}

  override def onCleanup(): Unit = {}
}
