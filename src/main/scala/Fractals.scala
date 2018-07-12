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

  override def onDraw(): Unit = {
    SDL_SetRenderDrawColor(renderer, 255.toUByte, 255.toUByte ,  255.toUByte, SDL_ALPHA_OPAQUE)

    SDL_RenderDrawLine(renderer, 0, 0, 800, 800)
    SDL_RenderPresent(renderer)
  }

  override def onIdle(): Unit = {}

  override def onCleanup(): Unit = {}
}
