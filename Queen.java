import java.util.ArrayList;

public class Queen extends LowerPiece {
    public Queen(Team team){
        super(team);
        this.imageFile = this.team == Team.WHITE ? "src/Chess_qlt60.png" : "src/Chess_qdt60.png";
        this.image = readImageFile(imageFile);
    }

    @Override
    public boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving) {
        return canCapture(p) && !routeIsBlocked(between);
    }

    @Override
    public boolean isMyRoute(Move m, Piece p) {
        if(m.equals(Move.STRAIGHT) || m.equals(Move.DIAGONAL)){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "q";
    }

    /*
     * purpose: tell CPU what my value is
     * result: int value
     */
    @Override
    public int myValue(){
        return 9;
    }

    /*
     * purpose: get possible moves
     * result: returns possible moves given
     */
    @Override
    public ArrayList<int[]> getMoves(Piece[][] pieces, int[] cors) {
        ArrayList<int[]> moveList = new Bishop(team).getMoves(pieces, cors);
        moveList.addAll(new Rook(team).getMoves(pieces, cors));
        return moveList;
    }
}
