mapping = [
    [5, 0, 1, 2, 3, 4],
    [1, 6, 7, 8, 9, 2],
    [3, 2, 9, 10, 11, 12],
    [15, 4, 3, 12, 13, 14],
    [18, 16, 5, 4, 15, 17],
    [21, 19, 20, 0, 5, 16],
    [20, 22, 23, 6, 1, 0],
    [7, 24, 25, 26, 27, 8],
    [9, 8, 27, 28, 29, 10],
    [11, 10, 29, 30, 31, 32],
    [13, 12, 11, 32, 33, 34],
    [37, 14, 13, 34, 35, 36],
    [39, 17, 15, 14, 37, 38],
    [42, 40, 18, 17, 39, 41],
    [44, 43, 21, 16, 18, 40],
    [45, 47, 46, 19, 21, 43],
    [46, 48, 49, 22, 20, 19],
    [49, 50, 51, 52, 23, 22],
    [23, 52, 53, 24, 7, 6]
]

nodes = set()
edges = set()

for m in mapping:
    for i in range(6):
        n1 = m[i]
        n2 = m[(i+1)%6]
        nodes.add(n1)
        edges.add((min(n1, n2), max(n1, n2)))

print(f"Total Unique Nodes: {len(nodes)}")
print(f"Nodes Range: {min(nodes)} to {max(nodes)}")
missing = [i for i in range(54) if i not in nodes]
print(f"Missing Nodes (0-53): {missing}")
print(f"Total Periodic Edges: {len(edges)}")
