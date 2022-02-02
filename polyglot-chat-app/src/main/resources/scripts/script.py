import io
import re
import traceback
import giphypop

from polyglot import export_value, import_value
from urllib import request
from urllib.parse import urlencode


SLASH_COMMAND_RE = re.compile("/([^ ]+)")
IMG_URL_RE = re.compile(b'(<img[^>]+alt=""[^>]+>)')

r_plot = import_value('toPlot')

def transform_message(sender, msg):
    match = SLASH_COMMAND_RE.match(msg)
    if match:
        try:
            command = match.group(1)
            args = msg.split(maxsplit=1)[1]
            if command == "img":
                req = request.Request(
                    f"http://www.google.de/search?{urlencode({'tbm': 'isch', 'q': args})}",
                    headers={'User-Agent': 'Dillo/3.0.5'},
                )
                with request.urlopen(req) as resp:
                    m = IMG_URL_RE.search(resp.read())
                    if m:
                        return m.group(0).decode("utf-8")
                    else:
                        return f"~No images found for {args}~"
            elif command == "gif":
                g = giphypop.Giphy()
                img = g.translate(phrase=args)
                if img:
                    return f"<img src='{img.media_url}'/>"
                else:
                    return f"No gif found for {args}"
            elif command == "=":
                return f"You sent a command with arguments: {args}"
            elif command == "help":
                return """
                /img TERM
                /= CODE
                """
            elif command == "plot":
                return r_plot(args)
            else:
                return f"[{sender}] Unknown command /{command}"
        except:
            error = io.StringIO()
            traceback.print_exc(file=error)
            return error.getvalue()
    return f"[{sender}] {msg}"


def validate(sender, senderTopic, message, receiver, receiverTopic):
    print(f"Called from python with {sender=}, {receiver=}, {message=}")
    return senderTopic == receiverTopic


class ChatMessageHandler:
    isValid = staticmethod(validate)
    createMessage = staticmethod(transform_message)


export_value("ChatMessageHandler", ChatMessageHandler)
