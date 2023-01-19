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

[
'''import numpy as np
from PIL import Image
import matplotlib.pyplot as plt

png = Image.open("imgs/pet.png")
png = png.quantize(colors=32)
png = png.convert(mode="RGB")
w,h = png.size
png''',
'''im = np.array(png)
im.shape
plt.imshow(im)
plt''',
'''# Calculate horizontal differences only finding increasing brightnesses
d = im[:,1:] - im[:,0:-1]
plt.close()
plt.imshow(d)
plt''',
'''# Calculate horizontal differences finding increasing or decreasing brightnesses
d = np.abs(im[:,1:].astype(np.int16) - im[:,0:-1].astype(np.int16))
plt.close()
plt.imshow(d)
plt
''',
'''# Convert to grayscale before calculating differences
plt.close()
im = np.array(png)
d = im.mean(axis=2)
d = np.abs(d[:,1:].astype(np.int16) - d[:,0:-1].astype(np.int16))
d2 = np.broadcast_to(d[..., np.newaxis], d.shape + (3,))
d2 = d2 * 4
plt.imshow(d2)
plt''',
'''# Calculate bi-directional edges in grayscale
plt.close()
im = np.array(png)
d = im.mean(axis=2)
d = d[:,1:] - d[:,0:-1]
d2 = np.broadcast_to(d[..., np.newaxis], d.shape + (3,))
plt.imshow(d2)
plt''',
'''# Show a histogram of the used colors
plt.close()
def hexencode(rgb):
    return "#%02x%02x%02x" % rgb

for idx,c in enumerate(reversed(sorted(png.getcolors(w*h), key=lambda a: a[0]))):
   # if idx == 0: continue
   if idx > 25: break
   plt.bar(idx, c[0], color=hexencode(c[1]),edgecolor=hexencode(c[1]))
plt''',
]
