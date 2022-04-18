import java.util.*
const val MASK_FROM=0x3F; const val MASK_TO=MASK_FROM shl 6; const val MASK_PIECE=0b1111 shl 12
const val MASK_ENCODING=0b1111 shl 16; const val NOT_A_FILE=-0x101010101010102
const val NOT_H_FILE=0x7f7f7f7f7f7f7f7f; const val MAX_MOVES=256
val DIAGONAL_MASKS=longArrayOf(-0x7fbfdfeff7fbfdff,0x4020100804020100,0x2010080402010000,0x1008040201000000,0x804020100000000,0x402010000000000,0x201000000000000,0x100000000000000,0x0,0x80,0x8040,0x804020,0x80402010,0x8040201008,0x804020100804,0x80402010080402)
val ANTI_DIAGONAL_MASKS=longArrayOf(0x102040810204080,0x1020408102040,0x10204081020,0x102040810,0x1020408,0x10204,0x102,0x1,0x0,0x8000000000000000UL.toLong(),0x4080000000000000,0x2040800000000000,0x1020408000000000,0x810204080000000,0x408102040800000,0x204081020408000)
val LSB64=Base64.getDecoder().decode("Px4DIDsOCyE8GDIJNxMVIj0dAjUzFykSOBwBKy4bACM+HzoEBTE2Bg80DCgHKi0QGTkwDQonCCwULyYWESUkGg==").map{it.toInt()}.toIntArray()
private val ONE_PAWN_ATTACKS=longArrayOf(0x2,0x5,0xA,0x14,0x28,0x50,0xA0,0x40)
private val P_FILES=arrayOf(NOT_A_FILE,NOT_H_FILE)
private val CASTLE_MODES=intArrayOf(156036,221316,184252,249532)
@OptIn(ExperimentalUnsignedTypes::class)class ChessBoard{var hmc=0; var fullMoveNumber=1; var color=0; var empty=0L; var castling=0; var enPassant=0L
    var board=Array(MAX_MOVES){IntArray(64)}; var occupancy=LongArray(MAX_MOVES)
    var colorOccupancy=Array(MAX_MOVES){LongArray(2)}; var pieceOcc=Array(MAX_MOVES){LongArray(13)}
    val previousCastling=IntArray(MAX_MOVES); val previousEnPassant=LongArray(MAX_MOVES)
    val moves=IntArray(MAX_MOVES){0}
    val makeMove={move:Int->previousCastling[hmc]=castling; previousEnPassant[hmc]=enPassant; moves[hmc]=move
        occupancy[hmc+1]=occupancy[hmc];
        System.arraycopy(pieceOcc[hmc],1,pieceOcc[hmc+1],1,12)
        System.arraycopy(colorOccupancy[hmc],0,colorOccupancy[hmc+1],0,2)
        System.arraycopy(board[hmc],0,board[hmc+1],0,64)
        fullMoveNumber+=++hmc%2
        val from=move and MASK_FROM
        val to=(move and MASK_TO)ushr 6
        enPassant=0
        val piece=(move and MASK_PIECE)ushr 12
        when(val encoding=(move and MASK_ENCODING)ushr 16){0->captureMove(from,to,piece,piece,color)
            1->{enPassant=(1L shl(to+(from-to)/2))
                captureMove(from,to,piece,piece,color)}
            2,3->{captureMove(from,to,piece,piece,color)
                val lookup=arrayOf(7,5,0,3,63,61,56,59)
                val i=(2*encoding-4)+(color*4)
                captureMove(lookup[i],lookup[i+1],4+color*6,4+color*6,color)}
            4->captureMove(from,to,piece,piece,color)
            5->{clear(to-(8*((color xor 1)*2-1)),color xor 1)
                captureMove(from,to,piece,piece,color)}
            6,7,8,9->captureMove(from,to,piece,((encoding-6)%4+2)+(color*6),color)
            10,11,12,13->{captureMove(from,to,piece,((encoding-6)%4+2)+(color*6),color)}}
        if(to in arrayOf(0,7,56,63))castling=castling and((2-(to%8)/7)shl to/56*2).inv()
        if(piece%6==0)castling=castling and(3 shl(color xor 1)*2)
        else if(piece%6==4&&from in arrayOf(0,7,56,63))castling=castling and((2-(from%8)/7)shl color*2).inv()
        color=color xor 1}
    fun undoMove(){castling=previousCastling[hmc-1]
        enPassant=previousEnPassant[hmc-1]
        empty=occupancy[hmc-1].inv()
        color=color xor 1
        fullMoveNumber-=hmc--%2}
    val getSingleRookAttacks={square:Int,friendlyOccupancyInv:Long->val fileMask=0x101010101010101 shl(square%8); val rankMask=255L shl((square/8)*8)
        O2ON(square,fileMask,rankMask,friendlyOccupancyInv)}
    val getSingleBishopAttacks={square:Int,friendlyOccupancyInv:Long->val m1=DIAGONAL_MASKS[((square/8)-(square%8))and 15]; val m2=ANTI_DIAGONAL_MASKS[((square/8)+(square%8))xor 7]
        O2ON(square,m1,m2,friendlyOccupancyInv)}
    val O2ON={square:Int,m1:Long,m2:Long,friendlyOccupancyInv:Long->val pieceBitboard=1L shl square
        (((((occupancy[hmc] and m1)-(2*pieceBitboard))xor(((occupancy[hmc] and m1).reverse()-(2*pieceBitboard.reverse())).reverse()))and m1)or((((occupancy[hmc] and m2)-(2*pieceBitboard))xor(((occupancy[hmc] and m2).reverse()-(2*pieceBitboard.reverse())).reverse()))and m2))and friendlyOccupancyInv}
    val getKingAttacks={piece:Int->val pieceBitboard=pieceOcc[hmc][piece]
        var attacks=((pieceBitboard shl 1)and NOT_A_FILE)or((pieceBitboard ushr 1)and NOT_H_FILE)or pieceBitboard
        attacks=attacks or(attacks shl 8)or(attacks ushr 8)
        attacks=attacks xor pieceBitboard
        attacks}
    val getAllKnightAttacks={knights:Long->val l1=(knights ushr 1)and 0x7f7f7f7f7f7f7f7f; val l2=(knights ushr 2)and 0x3f3f3f3f3f3f3f3f
        val r1=(knights shl 1)and-0x101010101010102; val r2=(knights shl 2)and-0x303030303030304
        val h1=l1 or r1; val h2=l2 or r2;
        (h1 shl 16)or(h1 ushr 16)or(h2 shl 8)or(h2 ushr 8)}
    val getCastleMoves={attackedSquares:Long,consumer:(Int)->Unit->val kingSafetyMasks=longArrayOf(0x70,0x1c,0x7000000000000000,0x1c00000000000000)
        val occupancyMask=longArrayOf(0x60,0xe,0x6000000000000000,0xe00000000000000)
        val castleTests=intArrayOf(1,2,4,8)
        repeat(2){if(((castleTests[color*2+it] and castling)!=0)and(attackedSquares and kingSafetyMasks[color*2+it]==0L)and(occupancy[hmc] and occupancyMask[color*2+it]==0L))
            consumer(CASTLE_MODES[color*2+it])}}
    fun pawnAttack(square:Int,color:Int)=shift(ONE_PAWN_ATTACKS[square%8],((square/8)*8)+8-16*color)
    fun getPawnAttacks(color:Int)=(shift(pieceOcc[hmc][1+color*6],-9*(color*2-1))and P_FILES[color])or(shift(pieceOcc[hmc][1+color*6],-7*(color*2-1))and P_FILES[color xor 1])
    fun singlePawnPushes(color:Int)=shift(shift(pieceOcc[hmc][1+color*6],-8*(color*2-1))and empty,-1*(-8*(color*2-1)))
    val doublePawnPushes={color:Int,singlePawnPushes:Long->val s=-16*(color*2-1); val destinations=shift(singlePawnPushes,s)and empty
        shift(destinations,-s)and(65280L shl(color*40))}
    val getPawnMoves={val moves=ArrayList<Int>(32)
        val maskSinglePush=singlePawnPushes(color)
        val maskDoublePush=doublePawnPushes(color,maskSinglePush)
        val opposingOccupancy=colorOccupancy[hmc][color xor 1]
        var maskPawns=pieceOcc[hmc][1+6*color]
        while(maskPawns!=0L){val square=bitscanForward(maskPawns)
            val pieceMask=(1L shl square)
            maskPawns=maskPawns and pieceMask.inv()
            if(maskDoublePush and pieceMask!=0L)
                moves.add(newMove(square,square+16-(32*color),1+6*color,1))
            if(maskSinglePush and pieceMask!=0L){val to=square+8-(16*color)
                if(to<8||to>55)moves.addAll((6..9).map{newMove(square,to,1+6*color,it)})
                else moves.add(newMove(square,to,1+6*color,0))}
            var pawnAttackMask=pawnAttack(square,color)and(opposingOccupancy or enPassant)
            while(pawnAttackMask!=0L){val to=bitscanForward(pawnAttackMask)
                val toMask=1L shl to
                pawnAttackMask=pawnAttackMask and toMask.inv()
                if(to<8||to>55)moves.addAll((10..13).map{newMove(square,to,1+6*color,it)})
                else{val encoding=if(toMask==enPassant)5 else 4
                    moves.add(newMove(square,to,1+6*color,encoding))}}}
        moves}
    fun isCheck(color:Int)=(getAttackedSquares(color xor 1)and pieceOcc[hmc][6+6*color])!=0L
    val getAttackedSquares={color:Int->var attacks=0L
        val otherKing=pieceOcc[hmc][6+(color xor 1)*6]
        occupancy[hmc]=occupancy[hmc] xor otherKing
        attacks=attacks or getPawnAttacks(color)
        attacks=attacks or getAllKnightAttacks(pieceOcc[hmc][2+color*6])
        for(i in 3..5){val piece=i+color*6
            var pieceBitboard=pieceOcc[hmc][piece]
            while(pieceBitboard!=0L){val square=bitscanForward(pieceBitboard)
                val pieceMask=(1L shl square)
                pieceBitboard=pieceBitboard and pieceMask.inv()
                attacks=attacks or when(piece){3,9->getSingleBishopAttacks(square,0L.inv())
                    4,10->getSingleRookAttacks(square,0L.inv())
                    5,11->getSingleBishopAttacks(square,0L.inv())or getSingleRookAttacks(square,0L.inv())
                    else->throw IllegalStateException("piece $piece is not a supported")}}}
        attacks=attacks or getKingAttacks(6+color*6)
        occupancy[hmc]=occupancy[hmc] or otherKing
        attacks}
    val getMoves={consumer:(Int)->Unit->val moves=mutableListOf<Int>()
        val opposingOccupancy=colorOccupancy[hmc][color xor 1]
        val friendlyMaskInv=colorOccupancy[hmc][color].inv()
        val attackedSquares=getAttackedSquares(color xor 1)
        val pieceRange=if(color==0)2..6 else 8..12
        for(piece in pieceRange){var pieceBitboard=pieceOcc[hmc][piece]
            while(pieceBitboard!=0L){val square=bitscanForward(pieceBitboard)
                val pieceMask=(1L shl square)
                pieceBitboard=pieceBitboard and pieceMask.inv()
                val bitboardMoves=when(piece){2,8->getAllKnightAttacks(pieceMask)
                    3,9->getSingleBishopAttacks(square,friendlyMaskInv)
                    4,10->getSingleRookAttacks(square,friendlyMaskInv)
                    5,11->getSingleBishopAttacks(square,friendlyMaskInv)or getSingleRookAttacks(square,friendlyMaskInv)
                    6,12->getKingAttacks(piece)and attackedSquares.inv()
                    else->throw IllegalStateException("piece $piece is not a supported")}and friendlyMaskInv
                var movesBitboard=bitboardMoves
                val capturesMoves=bitboardMoves and opposingOccupancy
                while(movesBitboard!=0L){val destination=bitscanForward(movesBitboard)
                    val destinationMask=1L shl destination
                    movesBitboard=movesBitboard and destinationMask.inv()
                    val encoding=if((capturesMoves and destinationMask)==0L)0 else 4
                    moves.add(newMove(square,destination,piece,encoding))}}}
        moves.addAll(getPawnMoves())
        moves.removeIf{makeMove(it)
            val check=isCheck(color xor 1)
            undoMove()
            check}
        moves.forEach(consumer)
        getCastleMoves(attackedSquares,consumer)}
    val captureMove={from:Int,to:Int,fromPiece:Int,toPiece:Int,color:Int->val fromBB=(1L shl from); val toBB=(1L shl to)
        pieceOcc[hmc][this[to]]=pieceOcc[hmc][this[to]] xor toBB
        pieceOcc[hmc][fromPiece]=pieceOcc[hmc][fromPiece] xor fromBB
        pieceOcc[hmc][toPiece]=pieceOcc[hmc][toPiece] or toBB
        occupancy[hmc]=(occupancy[hmc] xor fromBB)or toBB
        colorOccupancy[hmc][color]=colorOccupancy[hmc][color] xor(fromBB or toBB)
        colorOccupancy[hmc][color xor 1]=colorOccupancy[hmc][color xor 1] and toBB.inv()
        empty=occupancy[hmc].inv()
        board[hmc][from]=0
        board[hmc][to]=toPiece}
    val clear={square:Int,color:Int->val squareInvBB=(1L shl square).inv(); val oldPiece=this[square]
        occupancy[hmc]=occupancy[hmc] and squareInvBB
        colorOccupancy[hmc][color]=colorOccupancy[hmc][color] and squareInvBB
        pieceOcc[hmc][oldPiece]=pieceOcc[hmc][oldPiece] and squareInvBB
        empty=occupancy[hmc].inv()
        board[hmc][square]=0}
    operator fun set(square:Int,piece:Int){val fromBB=1L shl square; val inv=fromBB.inv(); val color=if(piece<=6)0 else 1
        pieceOcc[hmc][piece]=pieceOcc[hmc][piece] or fromBB
        occupancy[hmc]=occupancy[hmc] or fromBB
        colorOccupancy[hmc][color]=colorOccupancy[hmc][color] or fromBB
        colorOccupancy[hmc][color xor 1]=colorOccupancy[hmc][color xor 1] and inv
        empty=occupancy[hmc].inv()
        board[hmc][square]=piece}
    operator fun get(square:Int)=board[hmc][square]}
inline fun Long.reverse()=java.lang.Long.reverse(this)
fun bitscanForward(input:Long)=LSB64[((input xor(input-1)xor(input xor(input-1)ushr 32)).toUInt()*0x78291ACFU shr 26).toInt()]
fun newMove(from:Int,to:Int,piece:Int,encoding:Int)=(from and MASK_FROM)or((to and MASK_FROM)shl 6)or((piece and 0xF)shl 12)or((encoding and 0xF)shl 16)
inline fun shift(input:Long,shift:Int)=(input shl shift)or(input ushr(64-shift))
