 # Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 # DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 #
 # The Universal Permissive License (UPL), Version 1.0
 #
 # Subject to the condition set forth below, permission is hereby granted to any
 # person obtaining a copy of this software, associated documentation and/or
 # data (collectively the "Software"), free of charge and under any and all
 # copyright rights in the Software, and any and all patent rights owned or
 # freely licensable by each licensor hereunder covering either (i) the
 # unmodified Software as contributed to or provided by such licensor, or (ii)
 # the Larger Works (as defined below), to deal in both
 #
 # (a) the Software, and
 #
 # (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 # one is included with the Software each a "Larger Work" to which the Software
 # is contributed by such licensors),
 #
 # without restriction, including without limitation the rights to copy, create
 # derivative works of, display, perform, and distribute the Software and make,
 # use, sell, offer for sale, import, export, have made, and have sold the
 # Software and the Larger Work(s), and to sublicense the foregoing rights on
 # either these or other terms.
 #
 # This license is subject to the following condition:
 #
 # The above copyright notice and either this complete permission notice or at a
 # minimum a reference to the UPL must be included in all copies or substantial
 # portions of the Software.
 #
 # THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 # IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 # FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 # AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 # LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 # OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 # SOFTWARE.

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
