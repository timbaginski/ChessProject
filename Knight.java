public class Knight extends LowerPiece {
    public Knight(Team team){
        super(team);
        this.imageFile = this.team == Team.WHITE ? "src/Chess_nlt60.png" : "src/Chess_ndt60.png";
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
}
