import DrawingPrimitives.Point
import Fractals.{Fractal, sierpinski, vicsek}
import sdl2.Extras._
import sdl2.SDL._

import scala.scalanative.native._

object FractalsApp extends SdlApp(c"Fractals", 800, 800) with App {

  var animator:       Animator =  _
  var currentFractal: Fractal  =  vicsek
  var depth:          Int      =  0

  override def main(args: Array[String]): Unit = {
    animator = new Animator(currentFractal(0, depth, 800, new Point(0, 799)))

    super.main(args)
  }

  override def onEvent(event: Ptr[SDL_Event]): Unit = {
    event.type_ match {
      case SDL_QUIT =>
        SDL_Quit()
        System.exit(0)
      case SDL_KEYUP => {
        depth = (depth  + 1) % 8

        animator.lines = currentFractal(0, depth, 800, new Point(0, 799))
        animator.reset()
      }
      case _ =>
        ()
    }
  }

  override def onDraw(): Unit = {
    animator.draw(renderer)
    SDL_RenderPresent(renderer)
  }

  override def onIdle():    Unit = {}
  override def onCleanup(): Unit = {}
}
