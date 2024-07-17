import polyglot
from PIL import Image, ImageDraw, ImageFont
import requests
import io

"""
The PillowImageWrapper has methods to create, manipulate, and return an image. 
Instances of this class are meant to be used from Java using the PillowImageProxy interface.
"""


class PillowImageWrapper:

    # constructor
    def __init__(self, url):
        try:
            self._image = Image.open(requests.get(url, stream=True).raw)
        except:
            self._image = None
        self._original_image = self._image

    @property
    def image(self):
        return self._image

    @property
    def bytes(self):
        if self._image:
            # BytesIO is a file-like buffer stored in memory
            img_byte_arr = io.BytesIO()
            # image.save expects a file-like as a argument
            self._image.save(img_byte_arr, format='PNG')
            # Turn the BytesIO object back into a bytes object
            return img_byte_arr.getvalue()
        else:
            return b''

    """
    The `flip` method flips the image horizontally.
    """

    def flip(self):
        self._image = self._image.transpose(Image.FLIP_LEFT_RIGHT)

    """
    The `resize` method takes one argument: the scale to resize the original image.
    """

    def resize(self, scale):
        # get width and height
        width, height = self._original_image.size
        self._image = self._original_image.resize((round(width*scale), round(height*scale)))

    """
    The `watermark` method takes one argument: the text of the watermark.
    """

    def watermark(self, text):
        draw = ImageDraw.Draw(self._image)
        font = ImageFont.truetype('Monaco.ttf', 36)
        # draw watermark in the top left corner
        draw.text((0, 0), text, font=font, fill="red")


"""
Export the PillowImageWrapper class to Java
"""

polyglot.export_value("PillowImageWrapper", PillowImageWrapper)
