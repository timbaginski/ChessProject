import javax.swing.*;
public class Driver {

    /*
     * purpose: set components for JPanel
     */
    public static void placeComponents(JPanel panel, Board b){
        panel.setLayout(null);

    }

    public static void main(String[] args){
        // Creating Board
        Board b = new Board();
        // Creating JFrame
        JFrame frame = new JFrame();
        frame.setSize(250, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Creating JPanel
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, b);
        frame.setVisible(true);
    }

}
