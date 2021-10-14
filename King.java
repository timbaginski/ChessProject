import java.util.ArrayList;

public class King extends Piece {
    protected boolean check;

    public King(Team team){
        super(team);
        check = false;
        this.imageFile = this.team == Team.WHITE ? "Chess_klt60.png" : "Chess_kdt60.png";
        this.image = readImageFile(imageFile);
    }

    @Override
    public boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving) {
        int xDifference = myCors[0] - hisCors[0];
        int yDifference = myCors[1] - hisCors[1];
        return !routeIsBlocked(between) && canCapture(p) && Math.abs(xDifference) < 2 && Math.abs(yDifference) < 2;
    }

    @Override
    public boolean isMyRoute(Move m, Piece p) {
        return m.equals(Move.STRAIGHT) || m.equals(Move.DIAGONAL);
    }

    /*
     * purpose: tell me if I can be captured by a given team
     * input: Team t
     * result: boolean true of false
     */
    @Override
    public boolean isCapturedBy(Team t){
        return !t.equals(this.team);
    }

    @Override
    public String toString(){
        return "K";
    }

    /*
     * purpose: tell CPU what my value is
     * result: int value
     */
    @Override
    public int myValue(){
        return 0;
    }

    /*
     * purpose: get possible moves
     * result: returns possible moves given
     */
    @Override
    public ArrayList<int[]> getMoves(Piece[][] pieces, int[] cors, boolean isMoving) {
        ArrayList<int[]> moveList = new ArrayList<>();
        int[] temp = new int[]{cors[0] + 1, cors[1] + 1};
        if(temp[0] < 8 && temp[1] < 8 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] + 1, cors[1] - 1};
        if(temp[0] < 8 && temp[1] > 0 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] - 1, cors[1] + 1};
        if(temp[0] > 0 && temp[1] < 8 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] - 1, cors[1] - 1};
        if(temp[0] > 0 && temp[1] > 0 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] + 1, cors[1]};
        if(temp[0] < 8 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] - 1, cors[1]};
        if(temp[0] > 0 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0], cors[1] + 1};
        if(temp[1] < 8 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        temp = new int[]{cors[0], cors[1] - 1};
        if(temp[1] > 0 && canCapture(pieces[temp[0]][temp[1]])){
            moveList.add(temp);
        }
        return moveList;

    }
}
