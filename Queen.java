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
}
