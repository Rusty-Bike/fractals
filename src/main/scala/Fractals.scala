import Fractals.{lines, renderer}
import sdl2.SDL._
import sdl2.Extras._
import sdl2.SDL

import scala.scalanative.native._

object Fractals extends SdlApp(c"Fractals", 800, 800) with App {
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

  var animator: Animator =  _
  type Fractal = (Int, Int, Int, Point) => List[LineSegment]
  
  var currentFractal: Fractal = sierpinski
  
  var lines: List[LineSegment] = _
  var depth: Int = 0

  case class Point(x: Int, y: Int) {
  	def incX(amount: Int): Point = copy(x = x + amount)
  	def decX(amount: Int): Point = copy(x = x - amount)

  	def incY(amount: Int): Point = copy(y = y + amount)
  	def decY(amount: Int): Point = copy(y = y - amount)
  }

  class LineSegment(val start: Point, val end: Point)
  
  case class Triangle(bottomLeftPoint: Point, length: Int) {
  	def toLines: List[LineSegment] = {
      val topPoint         = Point(bottomLeftPoint.x + (length / 2), bottomLeftPoint.y - length)
      val bottomRightPoint = Point(bottomLeftPoint.x + length, bottomLeftPoint.y)

      val leftLine   = new LineSegment(bottomLeftPoint,  topPoint)
      val rightLine  = new LineSegment(bottomRightPoint, topPoint)
      val bottomLine = new LineSegment(bottomLeftPoint,  bottomRightPoint)
      
      List(bottomLine, leftLine, rightLine)
  	}
  }

  def sierpinski(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val triangle = Triangle(bottomLeftPoint, length)

    if (currentDepth == iterations) {
      triangle.toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = length / 2
      
      // Call ourselves again on the three sub-triangles
  
      val bottomLeftTriangle  = sierpinski(newDepth, iterations, newLength, bottomLeftPoint)
      val bottomRightTriangle = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.incX(newLength))
      val topCenterTriangle   = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.incX(newLength / 2).decY(newLength))
      
      // Merge and return the list of lines
      bottomLeftTriangle ::: topCenterTriangle ::: bottomRightTriangle
    }
  }


  override def main(args: Array[String]): Unit = {
    lines    = currentFractal(0, depth, 800, new Point(0, 799))
    animator = new Animator(lines)

    super.main(args)
  }

  override def onEvent(event: Ptr[SDL_Event]): Unit = {
    event.type_ match {
      case SDL_QUIT =>
        SDL_Quit()
        System.exit(0)
      case SDL_KEYUP => {
        depth = (depth  + 1) % 8
        lines = List()
        lines = currentFractal(0, depth, 800, new Point(0, 799))

        animator.lines = lines
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
