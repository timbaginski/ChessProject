public class Pawn extends LowerPiece {
    protected int forwardRange;

    public Pawn(Team team){
        super(team);
        this.forwardRange = 2;
        this.imageFile = team == Team.WHITE ? "src/Chess_plt60.png" : "src/Chess_pdt60.png";
        this.image = readImageFile(imageFile);
    }

    @Override
    public boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving) {
        int yDifference = myCors[0] - hisCors[0];
        int xDifference = myCors[1] - hisCors[1];
        if((yDifference < 0 && team.equals(Team.WHITE)) || (yDifference > 0 && team.equals(Team.BLACK))){
            return false;
        }
        if(routeIsBlocked(between) || !canCapture(p) || Math.abs(yDifference) > this.forwardRange){
            return false;
        }
        if(Math.abs(xDifference) > 1){
            return false;
        }
        if(isMoving){
            this.forwardRange = 1;
        }
        return true;
    }

    @Override
    public boolean isMyRoute(Move m, Piece p) {
        return (p == null && m.equals(Move.STRAIGHT)) || (p != null && m.equals(Move.DIAGONAL));
    }

    @Override
    public String toString(){
        return "p";
    }
}
