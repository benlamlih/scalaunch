package $package$

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

@main
def Client(): Unit = {
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement(),
  )
}

object Main {
  def appElement(): Div = {
    div(
      h1("Hello, world!"),
    )
  }
}
