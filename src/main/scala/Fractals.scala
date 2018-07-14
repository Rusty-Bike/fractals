import sdl2.SDL._
import sdl2.Extras._

import scala.scalanative.native._

object Fractals extends SdlApp(c"Fractals", 800, 800) with App {

  type Fractal = (Int, Int, Int, Point) => List[LineSegment]
  
  var currentFractal: Fractal = sierpinski
  
  var lines: List[LineSegment] = _
  var depth: Int = 0
  class Point(val x: Int, val y: Int)
  class LineSegment(val start: Point, val end: Point)


  def sierpinski(currentDepth: Int, iterations: Int, length: Int, leftPoint: Point): List[LineSegment] = {
    // topPoint
    val topPoint = new Point(leftPoint.x + (length / 2), leftPoint.y - (length))
    // BottomLeftPoint
    val bottomLeftPoint = leftPoint
    // BottomRightPoint
    val bottomRightPoint = new Point(leftPoint.x + length, leftPoint.y)

    if (currentDepth == iterations) {
      // add the left side
      val leftLine = new LineSegment(bottomLeftPoint, topPoint)
      // add the right side
      val rightLine = new LineSegment(bottomRightPoint, topPoint)
      // add the bottom
      val bottomLine = new LineSegment(bottomLeftPoint, bottomRightPoint)

      // return the list of line segments comprising the triangle
      List(leftLine, rightLine, bottomLine)
    } else {
      // Call ourselves again on the three sub-triangles
      // bottom left triangle
      val lines1 = sierpinski(
        currentDepth + 1,
        iterations,
        length / 2,
        bottomLeftPoint
      )

      // bottom right triangle
      val lines2 = sierpinski(
        currentDepth + 1,
        iterations,
        length / 2,
        new Point(
          bottomLeftPoint.x + length / 2,
          bottomLeftPoint.y
        )
      )

      // top center triangle
      val lines3 = sierpinski(
        currentDepth + 1,
        iterations,
        length / 2,
        new Point(
          bottomLeftPoint.x + (length / 4),
          bottomLeftPoint.y - (length / 2)
        )
      )
      // Merge and return the list of lines
      lines1 ::: lines2 ::: lines3
    }
  }


  override def main(args: Array[String]): Unit = {
    lines = currentFractal(0, 0, 800, new Point(0, 799))
    super.main(args)
  }

  override def onEvent(event: Ptr[SDL_Event]): Unit = {
    event.type_ match {
      case SDL_QUIT =>
        SDL_Quit()
        System.exit(0)
      case SDL_KEYUP => {
        depth = ((depth + 1) % 8)
        lines = currentFractal(0, depth, 800, new Point(0, 799))
      }
      case _ =>
        ()
    }
  }

  override def onDraw(): Unit = {
    SDL_SetRenderDrawColor(renderer, 0.toUByte, 0.toUByte, 0.toUByte, SDL_ALPHA_OPAQUE)
  
    SDL_RenderClear(renderer)
    
    SDL_SetRenderDrawColor(renderer, 255.toUByte, 255.toUByte, 255.toUByte, SDL_ALPHA_OPAQUE)

    lines.foreach(linesegment => SDL_RenderDrawLine(renderer, linesegment.start.x, linesegment.start.y, linesegment.end.x, linesegment.end.y))

    SDL_RenderPresent(renderer)
  }

  override def onIdle(): Unit = {}

  override def onCleanup(): Unit = {}
}
