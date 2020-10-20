package core

import core.DrawingPrimitives.{LineSegment, Point}
import fractals.Fractals._
import sdl2.Extras._
import sdl2.SDL._

import scala.scalanative.native._
import scala.util.Try

case class FractalInfo(name: String, code: Fractal)

case class Data(fractals: Array[FractalInfo], currentFractal: Int, depth: Int)

object FractalsApp extends SdlApp(c"Fractals", 800, 800) with App {
  var data: Data = _
  var fractalRenderer: Renderer = _
  var initialFractal: Int = 0

  def getLinesOfCurrentFractal: List[LineSegment] =
    data.fractals(
      data.currentFractal
    ).code(
      0,
      data.depth,
      770,
      Point(0, 798)
    )

  override def main(args: Array[String]): Unit = {

    initialFractal = Try(args(0).toInt).getOrElse(0)

    data = Data(
      // This may seem like it should be a map, but isn't for a couple of reasons:
      //   1. There will most likely be more data in the future(some fractals such as the H fractal
      //        would use different rendering options).
      //   2. The container needs to be ordered and indexed.
      fractals = Array(
        FractalInfo("Sierpinski",        sierpinski),
        FractalInfo("Vicsek",            vicsek),
        FractalInfo("Vicsek X",          vicsekx),
        FractalInfo("Cantor Dust",       cantorDust),
        FractalInfo("Koch Curve",        kochCurve),
        FractalInfo("Koch Snowflake",    kochSnowflake),
        FractalInfo("Tree",              tree),
        FractalInfo("Sierpinski Carpet", sierpinskiCarpet),
        FractalInfo("dragonCurve",       dragonCurve),
        FractalInfo("H",                 h),
        FractalInfo("Minkowski Sausage", minkowskiSausage),
        FractalInfo("Cesaro",            cesaro)
      ),
      currentFractal = initialFractal,
      depth = 0
    )

    fractalRenderer = new Renderer(getLinesOfCurrentFractal)

    super.main(args)
  }

  override def onEvent(event: Ptr[SDL_Event]): Unit = {
    event.type_ match {
      case SDL_QUIT =>
        SDL_Quit()
        System.exit(0)

      case SDL_KEYDOWN => {
        fractalRenderer.animate = !fractalRenderer.animate
        infoText.updateAnimationState(fractalRenderer.animate)

        fractalRenderer.lines = getLinesOfCurrentFractal
        fractalRenderer.reset()
      }
      case SDL_MOUSEBUTTONDOWN => {
        event.button.button match {
          case SDL_BUTTON_LEFT => {
            data = data.copy(depth = (data.depth + 1) % 8)

            fractalRenderer.lines = getLinesOfCurrentFractal
            fractalRenderer.reset()
          }
          case SDL_BUTTON_RIGHT => {
            data = data.copy(currentFractal = (data.currentFractal + 1) % data.fractals.length)

            fractalRenderer.lines = getLinesOfCurrentFractal
            fractalRenderer.reset()
          }
          case SDL_BUTTON_MIDDLE => {
            fractalRenderer.animate = !fractalRenderer.animate
            infoText.updateAnimationState(fractalRenderer.animate)

            fractalRenderer.lines = getLinesOfCurrentFractal
            fractalRenderer.reset()
          }
        }
      }
      case _ => ()
    }
  }

  override def onDraw(): Unit = {
    infoText.updateLinesNumber(fractalRenderer.linesToDraw)

    SDL_SetRenderDrawBlendMode(renderer, SDL_BLENDMODE_BLEND)

    fractalRenderer.draw(renderer)

    var rect1 = stackalloc[SDL_Rect].init(
      0,
      55,
      280,
      80
    )

    var rect2 = stackalloc[SDL_Rect].init(
      0,
      0,
      800,
      35
    )
    SDL_SetRenderDrawColor(renderer, 10.toUByte, 35.toUByte, 55.toUByte, 255.toUByte)

    SDL_RenderFillRect(renderer, rect1)
    SDL_RenderFillRect(renderer, rect2)
    infoText.draw(
      data,
      font,
      renderer
    ) // Must be after fractalRenderer.draw otherwise the text will be overwritten
    SDL_RenderPresent(renderer)
  }

  override def onIdle(): Unit = {}

  override def onCleanup(): Unit = {}

}
