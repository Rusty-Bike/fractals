object DrawingPrimitives {
  case class Point(x: Int, y: Int) {
    def moveRight(amount: Int): Point = copy(x = x + amount)
    def moveLeft(amount: Int):  Point = copy(x = x - amount)
    def moveDown(amount: Int):  Point = copy(y = y + amount)
    def moveUp(amount: Int):    Point = copy(y = y - amount)
  }

  case class LineSegment(start: Point, end: Point)

  case class Triangle(bottomLeftPoint: Point, length: Int) {
    def toLines: List[LineSegment] = {
      val topPoint         = bottomLeftPoint.moveRight(length / 2).moveUp(length)
      val bottomRightPoint = bottomLeftPoint.moveRight(length)

      val leftLine   = LineSegment(bottomLeftPoint,  topPoint)
      val rightLine  = LineSegment(bottomRightPoint, topPoint)
      val bottomLine = LineSegment(bottomLeftPoint,  bottomRightPoint)

      List(bottomLine, leftLine, rightLine)
    }
  }

  case class Square(bottomLeftPoint: Point, length: Int) {
    def toLines: List[LineSegment] = {
      val topLeftPoint     = bottomLeftPoint.moveUp(length)
      val topRightPoint    = topLeftPoint.moveRight(length)
      val bottomRightPoint = topRightPoint.moveDown(length)

      val leftLine   = LineSegment(bottomLeftPoint,  topLeftPoint)
      val rightLine  = LineSegment(bottomRightPoint, topRightPoint)
      val topLine    = LineSegment(topLeftPoint,     topRightPoint)
      val bottomLine = LineSegment(bottomLeftPoint,  bottomRightPoint)

      List(bottomLine, topLine, leftLine, rightLine)
    }
  }
}

