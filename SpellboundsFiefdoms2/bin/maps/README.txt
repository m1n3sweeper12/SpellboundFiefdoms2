Map File System Explanation


"-tiles" files are used to load tilemaps.
The first line represents the width and height of the map, in map sections.
The next section represents the lock boolean values for each section. 0 is unlocked, 1 is locked.
The third section represents the map section id for each map section.
0 is a default section, 1 is an enemy spawning section, 2 is a market section.
The fourth line represents the width and height of each section, in tiles.
The fifth line begins the sections data, with an array of tile ids for each tile in each section.

"-structs" files are used to load structures.
The first line represents the number of structures to be loaded.
Each section after that represents a different structure.

For each structure:
The first line represents the top-left tile coordinate of the structure.
The third line represents the width and height of the structure.
The fourth line represents the type of structure.
0 is a house, 1 is a shop, 2 is a park(???)
The following array represents the tile IDs for each tile in the structure.
0 is an empty tile, 1 is a wall tile, 2 is a door tile.