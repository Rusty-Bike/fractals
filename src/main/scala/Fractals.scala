import sdl2.SDL._
import sdl2.Extras._

import scala.scalanative.native._

object Fractals extends SdlApp(c"Fractals", 800, 800) with App {
  var lines: List[LineSegment] = _

  class Point(val x: Int, val y: Int) {
    override def toString = {
      s"Point($x, $y)"
    }
  }
  class LineSegment(val start: Point, val end: Point) {
    override def toString = {
      s"LineSegment($start, $end)"
    }
  }

  def sierpinski(currentDepth: Int, iterations: Int, length: Int, leftPoint: Point): List[LineSegment] = {
    // topPoint
    val topPoint = new Point(leftPoint.x + (length / 2), leftPoint.y - (length))
    // BottomLeftPoint
    val bottomLeftPoint = leftPoint
    // BottomRightPoint
    val bottomRightPoint = new Point(leftPoint.x + length, leftPoint.y)

    if (currentDepth == iterations) {


      // add the three lines of this triangle

      // add the left side
      val leftLine = new LineSegment(bottomLeftPoint, topPoint)
      // add the right side
      val rightLine = new LineSegment(bottomRightPoint, topPoint)
      // add the bottom
      val bottomLine = new LineSegment(bottomLeftPoint, bottomRightPoint)

      List(leftLine, rightLine, bottomLine)

    } else {
      // Call ourselves again on the three sub-triangles
      // bottom left triangle
      val lines1 = sierpinski(currentDepth + 1, iterations, length / 2, bottomLeftPoint)

      // bottom right triangle
      val lines2 = sierpinski(currentDepth + 1, iterations, length / 2, new Point(bottomLeftPoint.x + length / 2, bottomLeftPoint.y))
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

      lines1 ::: lines2 ::: lines3
    }
  }


  override def main(args: Array[String]): Unit = {
    lines = sierpinski(0, 7, 800, new Point(0, 800))
    super.main(args)
  }

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

    lines.foreach(linesegment => SDL_RenderDrawLine(renderer, linesegment.start.x, linesegment.start.y, linesegment.end.x, linesegment.end.y))

    SDL_RenderPresent(renderer)
  }

  override def onIdle(): Unit = {}

  override def onCleanup(): Unit = {}
}
