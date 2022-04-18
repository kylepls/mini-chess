# Mini Chess

> This project is a fully functional chess move generator with a focus on using as few lines of code as possible. Because of this, the code will be borderline unintelligible to anyone not familiar with bitboards or chess programming.

I was able to pack this into 276 lines of Kotlin. This is viewable [here](/src/main/kotlin/in/kyle/chess/ChessBoard.kt).

## What is a move generator?

A move generator is a program that generates all legal moves for a given position. This is useful
for tasks such as calculating the best move for a given position or checking if an arbitrary move
input is legal. The primary focus for a move generator is to be fast and efficient. It should be
able to generate millions of legal moves per second and be able to run at various tree depths. Chess
has a branching factor of about 33, this means that a move generator can only reach a full tree
depth of about 7.

## A high-level overview

The process of generating a legal move is as follows:

1. For all pieces on the board, generate a list of pseudo-legal moves. I.e.: all moves that are
   legal for the piece, but not necessarily legal for the board.
2. For all the moves generated, discard moves that are illegal for the board. I.e.: moves that would
   leave the king in check.

## Bitboards

Bitboards are referenced throughout this project. They are used to represent the position of pieces
on a chess board using a 64-bit integer. Since there's 64 squares on a chess board, there are 64
bits in a bitboard. Each bit represents a square on the board:

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

This allows for complex bitwise operations to be performed to manupulate chess pieces on the board.
For example, if there's a white knight on the E4 square, the bitboard for the E4 square would
be: `0x10000000`. We can then generate all the moves for the knight using the following bitwise
operations:

```kotlin
fun getAllKnightAttacks(knights: Long): Long {
   val l1 = (knights ushr 1) and 0x7f7f7f7f7f7f7f7f
   val l2 = (knights ushr 2) and 0x3f3f3f3f3f3f3f3f
   val r1 = (knights shl 1) and -0x101010101010102
   val r2 = (knights shl 2) and -0x303030303030304
   val h1 = l1 or r1
   val h2 = l2 or r2
   return (h1 shl 16) or (h1 ushr 16) or (h2 shl 8) or (h2 ushr 8)
}
```

For the example above, the knight at E4 would have a pseudo-legal move set of `0x284400442800`:

```
8 | . | . | . | . | . | . | . | . |
7 | . | . | . | . | . | . | . | . |
6 | . | . | . | X | . | X | . | . |
5 | . | . | X | . | . | . | X | . |
4 | . | . | . | . | . | . | . | . |
3 | . | . | X | . | . | . | X | . |
2 | . | . | . | X | . | X | . | . |
1 | . | . | . | . | . | . | . | . |
```

This shows just one example of how powerful bitboards can be. There's a ton of neat tricks under the
hood.

## Performance Testing

### What's Perft?

Perft is a chess evaluation function that calculates the number of legal moves that branch out to a
given depth for a given position. It is used to test the performance and validity of a move
generator as there is a well-known amount of moves from certain reference positions. You can think
about a perft test as visiting all the possible board states from a given starting point.

The perft code is very simple:

```kotlin
fun perft(board: ChessBoard, depth: Int): Long {
    if (depth == 0) return 1

    var nodes: Long = 0
    for (move in board.getMoves()) {
        board.makeMove(move)
        nodes += perft(board, depth - 1)
        board.undoMove()
    }

    return nodes
}
```

### Results

_All performance tests are done using an AMD Ryzen 2600._

| Test             | Depth | Nodes      | My Engine | Gigantua Time | My MNodes/S | Gigantua MNodes/S |
|------------------|-------|------------|-----------|---------------|-------------|-------------------|
| Initial Position | 1     | 20         | 48ms      | 2ms           | 0.000416    | 0.00936768        |
|                  | 2     | 400        | 42ms      | 1ms           | 0.009523    | 0.332502          |
|                  | 3     | 8902       | 62ms      | 1ms           | 0.143508    | 6.27786           |
|                  | 4     | 197281     | 201ms     | 6ms           | 0.981497    | 31.6561           |
|                  | 5     | 4865609    | 1553ms    | 37ms          | 3.133038    | 128.306           |
|                  | 6     | 119060324  | 28222ms   | 799ms         | 4.218706    | 148.849           |               
|                  | 7     | 3195901860 | 817700ms  | 146.138       | 3.908403    | 146.138           |
| Kiwipete         | 1     | 48         | 30ms      | 0ms           | 0.001600    | 4.000             | 
|                  | 2     | 2039       | 46ms      | 0ms           | 0.044326    | 75.5185           |
|                  | 3     | 97862      | 126ms     | 0ms           | 0.776683    | 115.403           |
|                  | 4     | 4085603    | 1432ms    | 21ms          | 2.853075    | 187.999           |
|                  | 5     | 193690690  | 48893ms   | 963ms         | 3.961522    | 200.929           |
|                  | 6     | 8031647685 | 1781665ms | 41264ms       | 4.507945    | 194.639           | 

[Gigantua Move Generator](https://github.com/Gigantua/Gigantua/tree/main/Gigantua)

Seen above, except the initial position, the move generator is able to generate millions of legal
moves per second. When compared to the fastest legal move generator, Gigantua, there isn't much of a
competition. In the highest depths, Gigantua is able to perform about 40x faster.

### A note on the performance difference

This project is not intended to be a benchmark of the best move generator. As stated above, the
primary focus is to be minimal. The fact that we see an even remotely comparable speed is a very
good result.

There are some things that can be improved to close the gap between the two engines. These are
outlined below:

1. The most significant of these is the pseudo-legal move generation step. In this implementation, a
   set of _pseudo-legal_ moves are generated for each position which are then pruned to only legal
   moves. This means that when a move is initially generated, it may leave the king in check. This
   adds a significant amount of time to the move generation process as many moves have to be
   discarded. Most high-performance engines use a _legal-only_ move generation step which means that
   the pruning step is not needed.
2. The move generation does not use magic bitboards. A magic bitboard is a lookup technique that
   allows for the quick generation of pseudo-legal sliding piece moves. As of right now, Hyperbola
   Quintessence is used to calculate sliding piece moves. Replacing this with a lookup-based
   approach would yield significant speed improvements.
3. My implementation is written in Kotlin and is not capiable of using machine-level instructions
   such as AVX or SSE. This means that the code lacks certain perforamnce optimizations that are
   used throughout other move generation engines.

## References and Useful Resources

* https://www.chessprogramming.org/Main_Page
* https://graphics.stanford.edu/~seander/bithacks.html
* https://www.codeproject.com/Articles/5313417/Worlds-Fastest-Bitboard-Chess-Movegenerator
* https://peterellisjones.com/posts/generating-legal-chess-moves-efficiently/
