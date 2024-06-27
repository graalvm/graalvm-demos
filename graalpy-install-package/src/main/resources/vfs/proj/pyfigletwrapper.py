import polyglot
import pyfiglet


"""
The PyfigletWrapper has two methods. Instances of this class are meant to be
 used from Java using the PyfigletProxy interface.
"""


class PyfigletWrapper:
    # This line just works around a GraalPy bug.
    # See https://github.com/oracle/graalpython/issues/403
    __import__('pathlib')._IGNORED_ERROS = __import__('pathlib')._IGNORED_ERROS + (5,)

    """
    The `format` method takes two parameters: a string to be rendered,
    and the name of the font to render the string.
    It returns a string
    """
    def format(self, text, font):
        # Returns a string
        return pyfiglet.figlet_format(text, font)



    """
    The `availableFonts` method takes no arguments.
    It returns a list of string
    """
    def availableFonts(self):
        # Returns a list of String
        return pyfiglet.FigletFont.getFonts()


"""
Export the PyfigletWrapper class to Java
"""

polyglot.export_value("PyfigletWrapper", PyfigletWrapper)
