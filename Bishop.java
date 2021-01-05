import java.util.ArrayList;

public class Bishop extends LowerPiece {
    public Bishop(Team team){
        super(team);
        this.imageFile = this.team == Team.WHITE ? "src/Chess_blt60.png" : "src/Chess_bdt60.png";
        this.image = readImageFile(imageFile);
    }

    @Override
    public boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving){
        return !routeIsBlocked(between) && canCapture(p);
    }

    @Override
    public boolean isMyRoute(Move m, Piece p) {
        if(m.equals(Move.DIAGONAL)){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "b";
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
    public ArrayList<int[]> getMoves(Piece[][] pieces, int[] cors, boolean isMoving){

        ArrayList<int[]> moveList = new ArrayList<>();
        int i, j;
        j = cors[1] + 1;
        for(i = cors[0] + 1; i < 8; i++){
            if(j > 7 || (!canCapture(pieces[i][j]))){
                break;
            }
            if(canCapture(pieces[i][j])){
                moveList.add(new int[]{i, j});
                break;
            }
            moveList.add(new int[]{i, j});
            j++;
        }
        j = cors[1] + 1;
        for(i = cors[0] - 1; i > -1; i--){
            if(j > 7 || (!canCapture(pieces[i][j]))){
                break;
            }
            if(canCapture(pieces[i][j])){
                moveList.add(new int[]{i, j});
                break;
            }
            moveList.add(new int[]{i, j});
            j++;
        }
        j = cors[1] - 1;
        for(i = cors[0] + 1; i < 8; i++){
            if(j < 0 || (!canCapture(pieces[i][j]))){
                break;
            }
            if(canCapture(pieces[i][j])){
                moveList.add(new int[]{i, j});
                break;
            }
            moveList.add(new int[]{i, j});
            j--;
        }
        j = cors[1] - 1;
        for(i = cors[0] - 1; i > 0; i--){
            if(j < 0 || (!canCapture(pieces[i][j]))){
                break;
            }
            if(canCapture(pieces[i][j])){
                moveList.add(new int[]{i, j});
                break;
            }
            moveList.add(new int[]{i, j});
            j--;
        }
        return moveList;
    }
}
