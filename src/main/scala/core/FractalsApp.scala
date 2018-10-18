package core

import core.DrawingPrimitives.{LineSegment, Point}
import fractals.Fractals._
import sdl2.Extras._
import sdl2.SDL._

import scala.scalanative.native._
import scala.util.Try

case class FractalInfo(val name: String, code: Fractal)
case class Data(val fractals: Array[FractalInfo], val currentFractal: Int, val depth: Int )

object FractalsApp extends SdlApp(c"Fractals", 800, 800) with App {
  var data: Data = _
  var fractalRenderer: Renderer       = _
  var initialFractal:  Int            = 0

  def getLinesOfCurrentFractal: List[LineSegment] =
    data.fractals(data.currentFractal).code(0, data.depth, 770, Point(0, 798)) // Changed third argument from 800 to 770 to give space to text

  override def main(args: Array[String]): Unit = {

    initialFractal = Try(args(0).toInt).getOrElse(0)

    data = Data(
      Array(
        FractalInfo("Sierpinski",        sierpinski),
        FractalInfo("Vicsek",            vicsek),
        FractalInfo("Vicsek X",          vicsekx),
        FractalInfo("Cantor Dust",       cantorDust),
        FractalInfo("Koch Curve",        kochCurve),
        FractalInfo("Koch Snowflake",    kochSnowflake),
        FractalInfo("Tree",              tree),
        FractalInfo("Sierpinski Carpet", sierpinskiCarpet)
      ),
      initialFractal,
      0
    )


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
            data = data.copy(depth = (data.depth + 1 ) % 8)

            fractalRenderer.lines = getLinesOfCurrentFractal
            fractalRenderer.reset()

          case SDL_BUTTON_RIGHT =>
            data = data.copy(currentFractal = (data.currentFractal + 1) % data.fractals.length)

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
    infoText.draw(data, font, renderer)  // Must be after fractalRenderer.draw otherwise the text will be overwritten
    SDL_RenderPresent(renderer)
  }

  override def onIdle(): Unit = {}

  override def onCleanup(): Unit = {}

}
