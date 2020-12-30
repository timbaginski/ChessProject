import java.util.*;

public class CPU {
    protected Team team;
    protected HashMap<int[], Integer> moves;
    protected int totalWeight;
    protected int direction;
    protected Board b;
    protected int[] kingCors;
    /*
     * purpose: initialize CPU
     * input: Team indicating team of CPU
     */
    public CPU(Team team, Board b){
        this.team = team;
        this.moves = new HashMap<>();
        this.totalWeight = 0;
        this.direction = this.team.equals(Team.WHITE) ? 1 : -1;
        this.b = b;
        this.kingCors = b.enemyKing(this.team);
    }

    /*
     * purpose: identify if piece is on my team
     * input: Piece p
     * result: true or false
     */
    public boolean hasPiece(Piece p){
        if(p == null){
            return false;
        }
        return !p.canCapture(new Pawn(this.team));
    }

    /*
     * purpose: figure out the weight of a given move
     * input: starting cors, ending cors, pieces
     * result: int weight
     */
    public int getWeight(int[] startCors, int[] endCors, Piece[][] pieces){
        return weightFromCapture(endCors, pieces) + weightFromBlocking(endCors, pieces)
                + weightFromAvoiding(startCors, endCors, pieces) + weightFromCheck(startCors, endCors, pieces);
    }

    /*
     * purpose: get weight that results from capturing piece
     * input: endCors, pieces
     * result: int weight
     */
    public int weightFromCapture(int[] endCors, Piece[][] pieces){
        int i0 = endCors[0];
        int i1 = endCors[1];
        if(pieces[i0][i1] == null){
            return 0;
        }
        totalWeight += pieces[i0][i1].myValue() * 7000;
        return pieces[i0][i1].myValue() * 7000;
    }

    /*
     * purpose: figure out the weight that results from blocking a piece
     * input: startCors, endCors, pieces
     */
    public int weightFromBlocking(int[] endCors, Piece[][] pieces){
        int e0 = endCors[0];
        int e1 = endCors[1];
        int blockValue = 0;
        int i = e0, j = e1;
        while(i > -1 && i < 8){
            if(pieces[i][e1] != null){
                blockValue += pieces[i][e1].myValue();
                break;
            }
            i += this.direction;
        }
        i = e0;
        j = e1 + 1;
        while(i > -1 && i < 8 && j > -1 && j < 8){
            if(pieces[i][j] != null){
                blockValue += pieces[i][j].myValue();
            }
            j++;
            i += this.direction;
        }
        i = e0;
        j = e1 - 1;
        while(i > -1 && i < 8 && j > -1 && j < 8){
            if(pieces[i][j] != null){
                blockValue += pieces[i][j].myValue();
            }
            j--;
            i += this.direction;
        }
        totalWeight += blockValue * 10;
        return blockValue * 10;
    }

    /*
     * purpose: get weight from avoiding capture
     * input: startCors, endCors, pieces
     * result: total weight from avoiding capture
     */
    public int weightFromAvoiding(int[] startCors, int[] endCors, Piece[][] pieces){
        int i, j;
        for(i = 0; i < 8; i++){
            for(j = 0; j < 8; j++){
                if(pieces[i][j] != null && !hasPiece(pieces[i][j]) && b.canMove(new int[]{i, j}, endCors, false)){
                    return 0;
                }
            }
        }
        totalWeight += pieces[startCors[0]][startCors[1]].myValue() * 1500;
        return pieces[startCors[0]][startCors[1]].myValue() * 1500;
    }

    /*
     * purpose: get weight from putting enemy in check
     * input: endCors
     * result: weight from putting in check (0 or 50)
     */
    public int weightFromCheck(int[] startCors, int[] endCors, Piece[][] pieces){
        Move move = b.typeOfMove(endCors, kingCors);
        Piece[] path = b.getBetweenPieces(b.getPath(endCors, kingCors, move));
        Piece p = pieces[startCors[0]][startCors[1]];
        if(!p.routeIsBlocked(path) && p.isMyRoute(move, new Pawn(this.team))){
            totalWeight += 5000;
            return 5000;
        }
        return 0;
    }

    /*
     * purpose: get all possible moves and find the weight
     * input: pieces
     */
    public void mapMoves(Piece[][] pieces){
        int i, j, k;
        List<int[]> pieceMoves;
        for(i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if(pieces[i][j] != null && hasPiece(pieces[i][j])) {
                    pieceMoves = pieces[i][j].getMoves(pieces, new int[]{i, j});
                    for(k = 0; k < pieceMoves.size(); k++){
                        moves.put(new int[]{i, j, pieceMoves.get(k)[0], pieceMoves.get(k)[1]},
                                getWeight(new int[]{i, j}, new int[]{pieceMoves.get(k)[0], pieceMoves.get(k)[1]}, pieces));
                    }
                }
            }
        }
    }

    /*
     * purpose: get coordinates representing the chosen move
     * input: pieces
     * result makes move on board, returns true or false for success or failure
     */
    public boolean makeMove(Piece[][] pieces){
        int[] chosenKey = new int[4];
        Random rand = new Random();
        mapMoves(pieces);
        System.out.println(moves.size());
        int moveID = rand.nextInt(totalWeight);
        int temp = 0;
        for(Map.Entry<int[], Integer> entry : moves.entrySet()){
            if(temp + entry.getValue() > moveID){
                chosenKey = entry.getKey();
                break;
            }
            temp += entry.getValue();
        }
        System.out.println(chosenKey[0] + " " + chosenKey[1] + " " + chosenKey[2] + " " + chosenKey[3]);
        return b.move(new int[]{chosenKey[0], chosenKey[1]}, new int[]{chosenKey[2], chosenKey[3]});
    }
}


