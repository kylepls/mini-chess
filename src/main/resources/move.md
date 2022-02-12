# Move format

| Position | Description          |
|----------|----------------------|
| 0-2      | From rank            |
| 3-5      | From file            |
| 6-8      | To rank              |
| 9-11     | To file              |
| 12-15    | Move nibble encoding |

# Move nibble encodings

| code | promotion | capture | special 1 | special 0 | kind of move         |
|------|-----------|---------|-----------|-----------|----------------------|
| 0    | 0         | 0       | 0         | 0         | quiet moves          |
| 1    | 0         | 0       | 0         | 1         | double pawn push     |
| 2    | 0         | 0       | 1         | 0         | king castle          |
| 3    | 0         | 0       | 1         | 1         | queen castle         |
| 4    | 0         | 1       | 0         | 0         | captures             |
| 5    | 0         | 1       | 0         | 1         | ep-capture           |
| 8    | 1         | 0       | 0         | 0         | knight-promotion     |
| 9    | 1         | 0       | 0         | 1         | bishop-promotion     |
| 10   | 1         | 0       | 1         | 0         | rook-promotion       |
| 11   | 1         | 0       | 1         | 1         | queen-promotion      |
| 12   | 1         | 1       | 0         | 0         | knight-promo capture |
| 13   | 1         | 1       | 0         | 1         | bishop-promo capture |
| 14   | 1         | 1       | 1         | 0         | rook-promo capture   |
| 15   | 1         | 1       | 1         | 1         | queen-promo capture  |
