import DrawingPrimitives.{LineSegment, Point}
import Fractals.{Fractal, sierpinski, vicsek, vicsekx}
import FractalsApp.FractalImpl.FractalImpl
import sdl2.Extras._
import sdl2.SDL._

import scala.scalanative.native._

object FractalsApp extends SdlApp(c"Fractals", 800, 800) with App {

  object FractalImpl extends Enumeration {
    type FractalImpl = Value
    val Sierpinski, Vicsek, VicsekX = Value
  }

  var fractals: Array[Fractal] = _
  var animator:       Animator =  _
  var currentFractal: Int = 0
  var depth:          Int      =  0

  override def main(args: Array[String]): Unit = {
    fractals= Array(sierpinski, vicsek, vicsekx)
    animator = new Animator(drawFractal(0, depth, 800, new Point(0, 799)))

    super.main(args)
  }

  override def onEvent(event: Ptr[SDL_Event]): Unit = {
    event.type_ match {
      case SDL_QUIT => {
        SDL_Quit()
        System.exit(0)
      }
      case SDL_MOUSEBUTTONDOWN => {

        if(event.button.button == SDL_BUTTON_LEFT) {
          depth = (depth + 1) % 8

          animator.lines = drawFractal(0, depth, 800, new Point(0, 799))
          animator.reset()
        } else if(event.button.button == SDL_BUTTON_RIGHT) {
          currentFractal = (currentFractal + 1) % fractals.length

          animator.lines = drawFractal(0, depth, 800, new Point(0, 799))
          animator.reset()

        }
      }
      case _ =>
        ()
    }
  }

  def drawFractal(currentDepth: Int, maxDepth: Int, width: Int, bottomLeftPoint: Point): List[LineSegment] = {
        fractals(currentFractal)(currentDepth, maxDepth, width, bottomLeftPoint)

  }
  override def onDraw(): Unit = {
    animator.draw(renderer)
    SDL_RenderPresent(renderer)
  }

  override def onIdle():    Unit = {}
  override def onCleanup(): Unit = {}
}
