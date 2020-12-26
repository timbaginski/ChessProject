import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class ChessListener implements MouseListener {
    /*
     * purpose: stores whether the mouse has been clicked
     */
    protected boolean isFirstClick;
    protected int[] clickCors;

    /*
     * purpose: creates a ChessListener setting clicked to false;
     */
    public ChessListener(){
        this.isFirstClick = false;
        this.clickCors = new int[2];
    }

    /*
     * purpose: tell board whether it is the first mouse click on a turn
     * result: returns boolean isFirstClick;
     */
    public boolean isFirstClick(){
        return this.isFirstClick;
    }

    /*
     * purpose: set clickCors to most recent click
     */
    public void setClickCors(MouseEvent e){
        this.clickCors = new int[]{e.getY(), e.getX()};
    }

    /*
     * purpose: tell Board the current Click Coordinates
     * result: returns int[] with the first index being x and second index being y
     */
    public int[] getClickCors(){
        return this.clickCors;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public void setFirstClick(){
        isFirstClick = !isFirstClick;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setFirstClick();
        setClickCors(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
