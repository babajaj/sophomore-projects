import numpy as np

A = [[[1, 1, 1],
      [1, 1, 1]],
      
     [[1, 1, 1],
      [1, 1, 1]],

     [[1, 0, 1],
      [0, 1, 0]]]

mean = np.mean(A, axis=(1, 2))

print(mean)