public class King extends Piece {
    protected boolean check;

    public King(Team team){
        super(team);
        check = false;
        this.imageFile = this.team == Team.WHITE ? "src/Chess_klt60.png" : "src/Chess_kdt60.png";
        this.image = readImageFile(imageFile);
    }

    @Override
    public boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving) {
        int xDifference = myCors[0] - hisCors[0];
        int yDifference = myCors[1] - hisCors[1];
        return routeIsBlocked(between) && canCapture(p) && xDifference <= 1 && yDifference <= 1;
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
        return false;
    }

    @Override
    public String toString(){
        return "K";
    }
}
