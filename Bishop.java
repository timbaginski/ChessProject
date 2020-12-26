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
}
