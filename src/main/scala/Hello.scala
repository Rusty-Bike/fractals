import sdl2.SDL._
import sdl2.Extras._

import scala.scalanative.native._

object Hello extends App {
  var window:   Ptr[SDL_Window]   = _
  var renderer: Ptr[SDL_Renderer] = _

  val title  = c"Fractals"
  val width  = 800
  val height = 800

  def init(): Unit = {

    SDL_Init(SDL_INIT_VIDEO)
    window   = SDL_CreateWindow(title, SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, width, height, SDL_WINDOW_SHOWN)
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_PRESENTVSYNC)
  }

  def loop(): Unit = {
    val event = stackalloc[SDL_Event]

    while (true) {
      while (SDL_PollEvent(event) != 0) {
        event.type_ match {
          case SDL_QUIT =>
            return
          case _ =>
            ()
        }
      }

      onDraw()
      onIdle()
      delay((1000 / 12).toUInt)
    }
  }

  def delay(ms: UInt): Unit =
    SDL_Delay(ms)

  def onDraw(): Unit = {}
  def onIdle(): Unit = {}

  def cleanup(): Unit = {
    SDL_DestroyRenderer(renderer)
    SDL_DestroyWindow(window)
    SDL_Quit()
  }

  init()
  loop()
  cleanup()
}
