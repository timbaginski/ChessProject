import java.util.ArrayList;

public class Pawn extends LowerPiece {
    protected int forwardRange;

    public Pawn(Team team) {
        super(team);
        this.forwardRange = 2;
        this.imageFile = team == Team.WHITE ? "src/Chess_plt60.png" : "src/Chess_pdt60.png";
        this.image = readImageFile(imageFile);
    }

    @Override
    public boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving) {
        int yDifference = myCors[0] - hisCors[0];
        int xDifference = myCors[1] - hisCors[1];
        if ((yDifference < 0 && team.equals(Team.WHITE)) || (yDifference > 0 && team.equals(Team.BLACK))) {
            return false;
        }
        if (routeIsBlocked(between) || !canCapture(p) || Math.abs(yDifference) > this.forwardRange) {
            return false;
        }
        if (Math.abs(xDifference) > 1) {
            return false;
        }
        if (isMoving) {
            this.forwardRange = 1;
        }
        return true;
    }

    @Override
    public boolean isMyRoute(Move m, Piece p) {
        return (p == null && m.equals(Move.STRAIGHT)) || (p != null && m.equals(Move.DIAGONAL));
    }

    @Override
    public String toString() {
        return "p";
    }

    /*
     * purpose: tell CPU what my value is
     * result: int value
     */
    @Override
    public int myValue() {
        return 1;
    }

    /*
     * purpose: get possible moves
     * result: returns possible moves given
     */
    @Override
    public ArrayList<int[]> getMoves(Piece[][] pieces, int[] cors, boolean isMoving) {
        ArrayList<int[]> moveList = new ArrayList<>();
        int direction = this.team.equals(Team.WHITE) ? -1 : 1;
        int[] temp = new int[]{cors[0] + direction, cors[1]};
        if(temp[0] < 8 && temp[0] > -1 && pieces[temp[0]][temp[1]] == null){
            moveList.add(temp);
        }
        if(forwardRange == 2){
            temp = new int[]{cors[0] + (direction * 2), cors[1]};
            if(moveList.size() == 1 && temp[0] < 8 && temp[0] > -1 && pieces[temp[0]][temp[1]] == null){
                moveList.add(temp);
            }
        }
        temp = new int[]{cors[0] + direction, cors[1] + 1};
        if(temp[0] < 8 && temp[0] > -1 && temp[1] < 8 && canCapture(pieces[temp[0]][temp[1]]) && pieces[temp[0]][temp[1]] != null){
            moveList.add(temp);
        }
        temp = new int[]{cors[0] + direction, cors[1] - 1};
        if(temp[0] < 8 && temp[0] > -1 && temp[1] > -1 && canCapture(pieces[temp[0]][temp[1]]) && pieces[temp[0]][temp[1]] != null){
            moveList.add(temp);
        }
        if(isMoving){
            forwardRange = 1;
        }
        return moveList;
    }
}