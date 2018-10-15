package core

import core.DrawingPrimitives.{LineSegment, Point}
import fractals.Fractals._
import sdl2.Extras._
import sdl2.SDL._

import scala.scalanative.native._
import scala.util.Try

object FractalsApp extends SdlApp(c"Fractals", 800, 800) with App {
  var fractals:        Array[Fractal] = _
  var fractalRenderer: Renderer       = _
  var currentFractal:  Int            = 0
  var depth:           Int            = 0

  def getLinesOfCurrentFractal: List[LineSegment] =
    fractals(currentFractal)(0, depth, 770, Point(0, 798)) // Changed third argument from 800 to 770 to give space to text

  override def main(args: Array[String]): Unit = {
    fractals = Array(sierpinski, vicsek, vicsekx, cantorDust, kochCurve, kochSnowflake, tree, sierpinskiCarpet)
    currentFractal = Try(args(0).toInt).getOrElse(0)
    fractalRenderer = new Renderer(getLinesOfCurrentFractal)

    super.main(args)
  }

  override def onEvent(event: Ptr[SDL_Event]): Unit = {
    event.type_ match {
      case SDL_QUIT =>
        SDL_Quit()
        System.exit(0)

      case SDL_MOUSEBUTTONDOWN =>
        event.button.button match {
          case SDL_BUTTON_LEFT =>
            depth = (depth + 1) % 8
            infoText.updateDepth(depth)

            fractalRenderer.lines = getLinesOfCurrentFractal
            fractalRenderer.reset()

          case SDL_BUTTON_RIGHT =>
            currentFractal = (currentFractal + 1) % fractals.length
            infoText.updateFractalName(currentFractal)

            fractalRenderer.lines = getLinesOfCurrentFractal
            fractalRenderer.reset()

          case SDL_BUTTON_MIDDLE =>
            fractalRenderer.animate = !fractalRenderer.animate
            infoText.updateAnimationState(fractalRenderer.animate)

            fractalRenderer.lines = getLinesOfCurrentFractal
            fractalRenderer.reset()
        }
      case _ =>
        ()
    }
  }

  override def onDraw(): Unit = {
    infoText.updateLinesNumber(fractalRenderer.linesToDraw)


    fractalRenderer.draw(renderer)
    infoText.draw(font, renderer)  // Must be after fractalRenderer.draw otherwise the text will be overwritten
    SDL_RenderPresent(renderer)
  }

  override def onIdle(): Unit = {}

  override def onCleanup(): Unit = {}

}
