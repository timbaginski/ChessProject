import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Piece {
    protected Team team;
    protected String imageFile;
    protected Image image;

    public Piece(Team team){
        this.team = team;
    }

    /*
     * purpose: find out if I can move
     * input: Piece p, array between
     * result: boolean can move or not
     */
    public abstract boolean canMove(int[] myCors, int[] hisCors, Piece[] between, Piece p, Move m, boolean isMoving);

    /*
     * purpose: find out if move is correct route
     * input: Piece p
     * result: false if not my route, true if my route
     */
    public abstract boolean isMyRoute(Move m, Piece p);

    /*
     * purpose: tell me if I can capture
     * input: Piece p
     * result: boolean if I can capture
     */
    public boolean canCapture(Piece p){
        if(p == null){
            return true;
        }
        return p.isCapturedBy(this.team);
    }

    /*
     * purpose: tell me if I can be captured by a given team
     * input: Team t
     * result: boolean true of false
     */
    public abstract boolean isCapturedBy(Team t);

    /*
     * purpose: String of me
     * input: just the Object
     * result: me as a String
     */
    @Override
    public String toString(){
        return "*";
    }

    /*
     * purpose: find out if my route is blocked
     * input: Piece[] between
     * result: boolean true or false;
     */
    public boolean routeIsBlocked(Piece[] between){
        int i;
        for(i = 0; i < between.length; i++){
            if(i != 0 && between[i] != null){
                return true;
            }
        }
        return false;
    }

    /*
     * purpose: get Image for board to read
     * result: returns Image file
     */
    public Image getImage(){
        return this.image;
    }

    /*
     * purpose: reads image file and extracts image
     */
    public Image readImageFile(String imageFile){
        try{
            return ImageIO.read(new File(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * purpose: figure out if it is my turn
     * input: Team t, indicating the current turn
     * result: true or false, depending on if it is my turn
     */
    public boolean isMyTurn(Team t){
        return this.team.equals(t);
    }

    /*
     * purpose: tell CPU what my value is
     * result: int value
     */
    public abstract int myValue();

    /*
     * purpose: get possible moves
     * result: returns possible moves given
     */
    public abstract ArrayList<int[]> getMoves(Piece[][] pieces, int[] cors, boolean isMoving);
}
