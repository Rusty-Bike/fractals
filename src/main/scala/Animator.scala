import DrawingPrimitives.LineSegment
import sdl2.Extras.SDL_ALPHA_OPAQUE
import sdl2.SDL
import sdl2.SDL.{SDL_RenderClear, SDL_RenderDrawLine, SDL_SetRenderDrawColor}

import scala.scalanative.native._
import scala.scalanative.native.{CInt, Ptr, Word}

class Animator(var lines: List[LineSegment]) {
  val backgroundColor: (CInt, CInt, CInt) = (0, 0, 0)
  val lineColor:       (CInt, CInt, CInt) = (255, 255, 255)

  var currentStep = 1
  val timeToWait  = 1

  var lastLineDrawTime: Word = System.currentTimeMillis()

  def reset(): Unit = currentStep = 1

  def draw( renderer: Ptr[SDL.SDL_Renderer]): Unit = {
    SDL_SetRenderDrawColor(renderer, 0.toUByte, 0.toUByte, 0.toUByte, SDL_ALPHA_OPAQUE)
    SDL_RenderClear(renderer)

    SDL_SetRenderDrawColor(renderer, 255.toUByte, 255.toUByte, 255.toUByte, SDL_ALPHA_OPAQUE)

    if(currentStep == lines.length) currentStep = 1


    val timeNow      = System.currentTimeMillis()
    val drawNextLine = (timeNow - lastLineDrawTime) > timeToWait

    if(drawNextLine) {
      currentStep = currentStep + 1
      lastLineDrawTime = timeNow
    }

    lines.view.take(currentStep).foreach(linesegment => SDL_RenderDrawLine(renderer, linesegment.start.x, linesegment.start.y, linesegment.end.x, linesegment.end.y))
  }
}
