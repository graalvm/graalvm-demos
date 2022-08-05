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
