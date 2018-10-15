package core

import sdl2.Extras.{SDL_INIT_VIDEO, SDL_RENDERER_PRESENTVSYNC, SDL_WINDOWPOS_CENTERED, SDL_WINDOW_SHOWN, SDL_Color}
import sdl2.SDL._
import sdl2.ttf.SDL_ttf._

import scala.scalanative.native.{Ptr, UInt, stackalloc, _}

abstract class SdlApp(title: CString, height: Int, width: Int) extends App {
  var window:   Ptr[SDL_Window]   = _
  var renderer: Ptr[SDL_Renderer] = _
  var font: Ptr[TTF_Font] = _

  val infoText: InfoText = new InfoText(width, height)

  def init(): Unit = {

    SDL_Init(SDL_INIT_VIDEO)
    window   = SDL_CreateWindow(title, SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, width, height, SDL_WINDOW_SHOWN)
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_PRESENTVSYNC)

    // SDL_TTF Initialization
    // If TTF fails to load, the program exites. But we could let it go on silently without showing any fonts.
    if(TTF_Init() != 0) {
      println("TTF_Init: %s".format(fromCString(SDL_GetError())))
      sys.exit(-1)
    }

    font = TTF_OpenFont(c"fonts/OpenSans-Regular.ttf", 20)
    // If TTF_OpenFont failed to load, the program exites. Again we can ignore the error and not show any text.
    if(font == null) {
      println("TTF_OpenFont failed: %s".format(fromCString(SDL_GetError())))
      sys.exit(-1)
    }

  }

  def onEvent(event: Ptr[SDL_Event]): Unit

  def loop(): Unit = {
    val event = stackalloc[SDL_Event]

    while (true) {
      while (SDL_PollEvent(event) != 0) {
        onEvent(event)
      }

      onDraw()
      onIdle()
      delay((1000 / 12).toUInt)
    }
  }

  def delay(ms: UInt): Unit =
    SDL_Delay(ms)

  def onDraw(): Unit
  def onIdle(): Unit
  def onCleanup(): Unit

  def cleanup(): Unit = {
    if(font != null) { TTF_CloseFont(font) }
    TTF_Quit()
    SDL_DestroyRenderer(renderer)
    SDL_DestroyWindow(window)
    onCleanup()
    SDL_Quit()
  }

  init()
  loop()
  cleanup()
}
