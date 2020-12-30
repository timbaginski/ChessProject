import java.util.ArrayList;

public class Rook extends LowerPiece {
    public Rook(Team team){
        super(team);
        this.imageFile = this.team == Team.WHITE ? "src/Chess_rlt60.png" : "src/Chess_rdt60.png";
        this.image = readImageFile(imageFile);
    }

    @Override
    public boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving) {
        return !routeIsBlocked(between) && canCapture(p);
    }

    @Override
    public boolean isMyRoute(Move m, Piece p) {
        if(m.equals(Move.STRAIGHT)){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "r";
    }

    /*
     * purpose: tell CPU what my value is
     * result: int value
     */
    @Override
    public int myValue(){
        return 5;
    }

    /*
     * purpose: get possible moves
     * result: returns possible moves given
     */
    @Override
    public ArrayList<int[]> getMoves(Piece[][] pieces, int[] cors) {
        ArrayList<int[]> moveList = new ArrayList<>();
        int i;
        for(i = cors[0] + 1; i < 8; i++){
            if(!canCapture(pieces[i][cors[1]])){
                break;
            }
            moveList.add(new int[]{i, cors[1]});
        }
        for(i = cors[0] - 1; i > 0; i--){
            if(!canCapture(pieces[i][cors[1]])){
                break;
            }
            moveList.add(new int[]{i, cors[1]});
        }
        for(i = cors[1] + 1; i < 8; i++){
            if(!canCapture(pieces[cors[0]][i])){
                break;
            }
            moveList.add(new int[]{cors[0], i});
        }
        for(i = cors[1] - 1; i > 0; i--){
            if(!canCapture(pieces[cors[0]][i])){
                break;
            }
            moveList.add(new int[]{cors[0], i});
        }
        return moveList;
    }
}
