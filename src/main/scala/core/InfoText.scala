package core

import sdl2.SDL
import sdl2.Extras._
import sdl2.SDL._
import sdl2.ttf.SDL_ttf._

import scala.scalanative.native._

/**
 * Draws the HUD on the screen.
 * @param fontColor The font color to use
 * @param width The width of the HUD text block
 * @param height The height of the HUD text block
 */
class InfoText(
  fontColor: SDL_Color,
  width:     Int,
  height:    Int
) {
  var numberOfLines = 0

  /**
   * Sets the number of lines currently being drawn to the screen.
   * @param linesToDraw
   */
  def updateLinesNumber(linesToDraw: CInt): Unit = numberOfLines = linesToDraw


  /**
   * Constructor OverLoad. If InfoText is instantiated without a color,
   * fontColor is white.
   *
   * @param width The width of the HUD text block
   * @param height The height of the HUD text block
   */
  def this(width: Int, height: Int){
    this(
      SDL_Color(
        255.toUByte,
        255.toUByte,
        255.toUByte,
        255.toUByte
      ),
      width,
      height
    ) // RGB color white: (255, 255, 255)
  }

  var isAnimationOn: String  = "off"

  /**
   * Toggles whether or not we are animating when drawing.
   * @param b Whether or not we are animating.
   */
  def updateAnimationState(b: Boolean): Unit = {
    isAnimationOn = if(b) "On" else "Off"
  }

  /**
   * Draws the HUD onto the screen.
   *
   * @param data The data to draw from
   * @param font The font to use
   * @param renderer The SDL renderer to draw with
   * @return
   */
  def draw(
    data:     Data,
    font:     Ptr[TTF_Font],
    renderer: Ptr[SDL.SDL_Renderer]
  ): CInt = {
    // Prepare the texture with the text
    Zone { implicit z =>
      val w = stackalloc[CInt]
      val h = stackalloc[CInt]

      val nameAndDepthInfo : CString = toCString(
        s"${data.fractals(data.currentFractal).name} Depth: ${data.depth}"
      )(z)

      val message = TTF_RenderText_Solid(
        font,
        nameAndDepthInfo,
        fontColor
      )

      val texture = SDL_CreateTextureFromSurface(
        renderer,
        message
      )

      SDL_QueryTexture(
        texture,
        null,
        null,
        w,
        h
      )

      var rect = stackalloc[SDL_Rect].init(
        10,
        3,
        !w,
        !h
      )

      SDL_RenderCopy(
        renderer,
        texture,
        null,
        rect
      )

      val animationAndLinesInfo: CString = toCString(
        s"Animation: ${isAnimationOn}    Number of Lines: ${numberOfLines}"
      )(z)

      val message2: Ptr[SDL_Surface] = TTF_RenderText_Solid(
        font,
        animationAndLinesInfo,
        fontColor
      )

      var texture2: Ptr[SDL_Texture] = SDL_CreateTextureFromSurface(
        renderer,
        message2
      )

      SDL_QueryTexture(
        texture2,
        null,
        null,
        w,
        h
      )

      rect = stackalloc[SDL_Rect].init(
        width - 10 - !w,
        0,
        !w,
        !h
      )

      SDL_RenderCopy(
        renderer,
        texture2,
        null,
        rect
      )

      val instructions: List[String] = List(
        s"LMB: Increase Fractal Depth",
        s"RMB: Cycle Fractal",
        s"MMB: Toggle Animation"
      )

      val retCode: Int = 0

      val results: List[Int] = instructions.zipWithIndex.map(
        {
          case (instruction, index) => {
            drawText(
              instruction,
              10,
              60 + index * 20,
              font,
              renderer
            )
          }
        }
      ).filter(_ != 0)

      if (results.length > 0) results.head
      0
    }
  }

  /**
   * Draws a line of text to the screen
   * @param text The text to draw
   * @param x The x value of the start of the text
   * @param y The y value of the start of the text
   * @param font The font to use
   * @param renderer The SDL renderer to draw with.
   * @return
   */
  def drawText(
    text:     String,
    x:        CInt ,
    y:        CInt,
    font:     Ptr[TTF_Font],
    renderer: Ptr[SDL.SDL_Renderer]
  ): CInt = {
    Zone { implicit z =>
      val w = stackalloc[CInt]
      val h = stackalloc[CInt]

      val ctext: CString = toCString(text)(z)

      val message = TTF_RenderText_Solid(
        font,
        ctext,
        fontColor
      )

      val texture = SDL_CreateTextureFromSurface(renderer, message)

      SDL_QueryTexture(
        texture,
        null,
        null,
        w,
        h
      )

      var rect = stackalloc[SDL_Rect].init(
        x,
        y,
        !w,
        !h
      )

      SDL_RenderCopy(
        renderer,
        texture,
        null,
        rect
      )
    }
  }
}
