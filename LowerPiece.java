public abstract class LowerPiece extends Piece {
    public LowerPiece(Team team){
        super(team);
    }

    /*
     * purpose: tell me if I can be captured by a given team
     * input: Team t
     * result: boolean true of false
     */
    @Override
    public boolean isCapturedBy(Team t){
        return !this.team.equals(t);
    }

}
