import java.util.ArrayList;

public class Knight extends LowerPiece {
    public Knight(Team team){
        super(team);
        this.imageFile = this.team == Team.WHITE ? "Chess_nlt60.png" : "Chess_ndt60.png";
        this.image = readImageFile(imageFile);
    }

    @Override
    public boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving) {
        return canCapture(p);
    }

    @Override
    public boolean isMyRoute(Move m, Piece p) {
        if(m.equals(Move.KNIGHT)){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "k";
    }

    /*
     * purpose: tell CPU what my value is
     * result: int value
     */
    @Override
    public int myValue(){
        return 3;
    }

    /*
     * purpose: get possible moves
     * result: returns possible moves given
     */
    @Override
    public ArrayList<int[]> getMoves(Piece[][] pieces, int[] cors, boolean isMoving) {
        ArrayList<int[]> moveList = new ArrayList<>();
        int[] temp = new int[]{cors[0] + 2, cors[1] + 1};
        if(temp[0] < 8 && temp[1] < 8 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] - 2, cors[1] + 1};
        if(temp[0] > -1 && temp[1] < 8 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] - 2, cors[1] - 1};
        if(temp[0] > -1 && temp[1] > -1 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] + 2, cors[1] - 1};
        if(temp[0] < 8 && temp[1] > -1 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] + 1, cors[1] - 2};
        if(temp[0] < 8 && temp[1] > -1 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] - 1, cors[1] - 2};
        if(temp[0] > -1 && temp[1] > -1 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] - 1, cors[1] + 2};
        if(temp[0] > -1 && temp[1] < 8 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] + 1, cors[1] + 2};
        if(temp[0] < 8 && temp[1] < 8 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        return moveList;
    }
}
