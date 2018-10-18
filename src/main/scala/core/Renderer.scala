package core

import core.DrawingPrimitives.LineSegment
import sdl2.Extras.SDL_ALPHA_OPAQUE
import sdl2.SDL
import sdl2.SDL.{SDL_RenderClear, SDL_RenderDrawLine, SDL_SetRenderDrawColor}

import scala.scalanative.native.{CInt, Ptr, Word, _}

class Renderer(var lines: List[LineSegment], var animate: Boolean = false) {
  val backgroundColor: (CInt, CInt, CInt) = (0, 0, 0)
  val lineColor:       (CInt, CInt, CInt) = (255, 255, 255)

  var linesToDraw = 1
  val timeToWait  = 1

  var lastLineDrawTime: Word = System.currentTimeMillis()

  def reset(): Unit = linesToDraw = 1

  def draw( renderer: Ptr[SDL.SDL_Renderer]): Unit = {
    SDL_SetRenderDrawColor(renderer, 0.toUByte, 0.toUByte, 0.toUByte, SDL_ALPHA_OPAQUE)
    SDL_RenderClear(renderer)

    SDL_SetRenderDrawColor(renderer, 255.toUByte, 255.toUByte, 255.toUByte, SDL_ALPHA_OPAQUE)

    if(animate) {
      if (linesToDraw == lines.length) linesToDraw = 1

      val timeNow      = System.currentTimeMillis()
      val drawNextLine = (timeNow - lastLineDrawTime) > timeToWait

      if(drawNextLine) {
        // If we are animating and it is time to draw another line,
        // increase the number of lines that will be pulled from the pile of lines
        // and drawn.
        linesToDraw = linesToDraw + 1
        lastLineDrawTime = timeNow
      }
    }else {
      // If we are not animating, then grab and draw all of the lines.
     linesToDraw = lines.length
    }

    // Grab 'currentStep' number of lines from the line pile and draw them.
    lines.view.take(linesToDraw).foreach { linesegment =>
      SDL_RenderDrawLine(
        renderer,
        linesegment.start.x,
        linesegment.start.y,
        linesegment.end.x,
        linesegment.end.y
      )
    }
  }
}
