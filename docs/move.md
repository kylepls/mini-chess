# Pieces

| Piece  | Code | Binary |
|--------|------|--------|
| Pawn   | 1    | 001    |
| Knight | 2    | 010    |
| Bishop | 3    | 011    |
| Rook   | 4    | 100    |
| Queen  | 5    | 101    |
| King   | 6    | 110    |

# Colored Pieces

| Piece        | Code | Binary |
|--------------|------|--------|
| White Pawn   | 1    | 0001   |
| White Knight | 2    | 0010   |
| White Bishop | 3    | 0011   |
| White Rook   | 4    | 0100   |
| White Queen  | 5    | 0101   |
| White King   | 6    | 0110   |
| Black Pawn   | 9    | 1001   |
| Black Knight | 10   | 1010   |
| Black Bishop | 11   | 1011   |
| Black Rook   | 12   | 1100   |
| Black Queen  | 13   | 1101   |
| Black King   | 14   | 1110   |

# Move format

| Position | Description   |
|----------|---------------|
| 0-5      | From position |
| 6-11     | To position   |
| 12-15    | Piece         |
| 16-19    | Encoding      |

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

# Board Indices

| .   | .   | .   | .   | .   | .   | .   | .   | .   |
|-----|-----|-----|-----|-----|-----|-----|-----|-----|
| 8   | 56  | 57  | 58  | 59  | 60  | 61  | 62  | 63  |
| 7   | 48  | 49  | 50  | 51  | 52  | 53  | 54  | 55  |
| 6   | 40  | 41  | 42  | 43  | 44  | 45  | 46  | 47  |
| 5   | 32  | 33  | 34  | 35  | 36  | 37  | 38  | 39  |
| 4   | 24  | 25  | 26  | 27  | 28  | 29  | 30  | 31  |
| 3   | 16  | 17  | 18  | 19  | 20  | 21  | 22  | 23  |
| 2   | 8   | 9   | 10  | 11  | 12  | 13  | 14  | 15  |
| 1   | 0   | 1   | 2   | 3   | 4   | 5   | 6   | 7   |
| .   | A   | B   | C   | D   | E   | F   | G   | H   |

