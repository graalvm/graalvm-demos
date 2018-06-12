import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object GreetingMacros {
  def greeting: String = macro greetingMacro

  def greetingMacro(c: Context): c.Tree = {
    import c.universe._

    q"""
     "Hello, World!"
     """
  }
}

