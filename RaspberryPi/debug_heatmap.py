  import time
import busio
import board
import adafruit_amg88xx
import numpy as np
import matplotlib
import matplotlib.pyplot as plt
#import seaborn as sb

i2c = busio.I2C(board.SCL, board.SDA)
amg = adafruit_amg88xx.AMG88XX(i2c)

fig = plt.figure()
ax = fig.add_subplot(111)
reverseArray = []

goodArray = np.array(amg.pixels)
goodArray = np.fliplr(goodArray)

im = ax.imshow(goodArray, interpolation='lanczos', vmin=20, vmax=30)

textArray = [ [ None ] * goodArray.shape[0] for j in range(goodArray.shape[0]) ]
for y in range(goodArray.shape[0]):
    for x in range(goodArray.shape[1]):
        textArray[x][y] = ax.text(x, y, goodArray[x, y],
                                  ha="center", va="center", color="w")

plt.show(block=False)

while True:
    for row in amg.pixels:
        # Pad to 1 decimal place
        print(['{0:.1f}'.format(temp) for temp in row])
        print("")
    print("\n")
    
    time.sleep(0.1)
    
    reverseArray = []

    goodArray = np.array(amg.pixels)
    goodArray = np.fliplr(goodArray)
    
    im.set_array(goodArray)
    for y in range(goodArray.shape[0]):
        for x in range(goodArray.shape[1]):
            textArray[x][y].set_text("")
    
    fig.canvas.draw()

