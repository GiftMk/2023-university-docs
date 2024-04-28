from itertools import product
from random import shuffle

distances = [64, 128, 256, 512]
widths = [8, 16, 32]

combinations = list(product(distances, widths))

for combination in combinations:
    print(combination)

print(f"\n{len(combinations)}")
