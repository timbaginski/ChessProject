import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Board extends JPanel {
    protected Piece[][] pieces;
    protected List<int[][]> threats;
    protected final int LENGTH = 8;
    protected final int WIDTH = 8;
    protected int[] kingW, kingB;
    protected Team turn;

    public Board(){
        turn = Team.WHITE;
        pieces = new Piece[LENGTH][WIDTH];
        kingW = new int[]{7, 3};
        kingB = new int[]{0, 4};
        threats = new ArrayList<>();
        initPieces();
    }

    /*
     * purpose: initialize pieces
     * input: none
     * result: pieces if filled
     */
    public void initPieces(){
        Team w = Team.WHITE;
        Team b = Team.BLACK;
        Piece[] blackOrder = {new Rook(b), new Knight(b), new Bishop(b), new Queen(b), new King(b), new Bishop(b),
                                new Knight(b), new Rook(b)};
        Piece[] whiteOrder = {new Rook(w), new Knight(w), new Bishop(w), new King(w), new Queen(w), new Bishop(w),
                                new Knight(w), new Rook(w)};
        pieces[0] = blackOrder;
        Arrays.fill(pieces[1], new Pawn(b));
        Arrays.fill(pieces[6], new Pawn(w));
        pieces[7] = whiteOrder;
        int i;
        for(i = 2; i < 6; i++){
            Arrays.fill(pieces[i], null);
        }
    }

    /*
     * purpose: attempt to move a piece
     * input: coordinates of piece, coordinates of desired location, whether the piece is actually intended to be moved
     * result: return true of valid move, false otherwise
     */
    public boolean canMove(int[] currentCors, int[] newCors, boolean isMoving){
        if(isInBounds(currentCors, newCors)){
            return false;
        }
        Move m = typeOfMove(currentCors, newCors);
        Piece p = pieces[currentCors[0]][currentCors[1]];
        if(p == null || !p.isMyTurn(this.turn)){
            return false;
        }
        Piece p2 = pieces[newCors[0]][newCors[1]];
        Piece[] betweenP = getBetweenPieces(getPath(currentCors, newCors, m));
        if(!p.isMyRoute(m, p2)){
            return false;
        }
        return p.canMove(currentCors, newCors, betweenP, p2, m, isMoving);
    }

    /*
     * purpose: if a piece can move, move it
     * input: currentCors, newCors
     * result: Piece is moved to a new Square
     */
    public boolean move(int[] currentCors, int[] newCors){
        if(canMove(currentCors, newCors, true)){
            pieces[newCors[0]][newCors[1]] = pieces[currentCors[0]][currentCors[1]];
            pieces[currentCors[0]][currentCors[1]] = null;
            int[] king = turn.equals(Team.WHITE) ? kingW : kingB;
            if(currentCors == king){
                king = newCors;
                kingW = turn.equals(Team.WHITE) ? king : kingW;
                kingB = turn.equals(Team.WHITE) ? kingB : king;
            }
            turn = turn.equals(Team.WHITE) ? Team.BLACK : Team.WHITE;
            System.out.println("valid move");
            return true;
        }
        System.out.println("invalid move");
        return false;
    }

    /*
     * purpose: figure out if either cor is out of bounds
     * input: currentCors, newCors
     * result: true if possible, false otherwise
     */
    public boolean isInBounds(int[] currentCors, int[] newCors){
        return currentCors[0] > 7 || currentCors[0] < 0 || currentCors[1] > 7 || currentCors[1] < 0
                || newCors[0] > 7 || newCors[0] < 0 || newCors[1] > 7 || newCors[1] < 0;
    }

    /*
     * purpose: find out if move is Knight
     * input: currentCors, newCors
     * result: true or false, Knight or noy
     */
    public boolean isKnight(int[] currentCors , int[] newCors){
        int xDifference = Math.abs(currentCors[0] - newCors[0]);
        int yDifference = Math.abs(currentCors[1] - newCors[1]);
        return (xDifference == 1 && yDifference == 2) || (xDifference == 2 && yDifference == 1);
    }

    /*
     * purpose: find out if move is diagonal
     * input: currentCors, newCors
     * result: true or false, diagonal or not
     */
    public boolean isDiagonal(int[] currentCors, int[] newCors){
        int xDifference = Math.abs(currentCors[0] - newCors[0]);
        int yDifference = Math.abs(currentCors[1] - newCors[1]);
        return xDifference != 0 && yDifference / xDifference == 1;
    }

    /*
     * purpose: find out if move is straight or not
     * input: currentCors, newCors
     * result: true or false, straight or not
     */
    public boolean isStraight(int[] currentCors, int[] newCors){
        int xDifference = Math.abs(currentCors[0] - newCors[0]);
        int yDifference = Math.abs(currentCors[1] - newCors[1]);
        return xDifference == 0 || yDifference == 0;
    }

    /*
     * purpose: get the type of move performed
     * input: currentCors, newCors
     * result: return type of move
     */
    public Move typeOfMove(int[] currentCors, int[] newCors){
        if(isKnight(currentCors, newCors)){
            return Move.KNIGHT;
        }
        if(isStraight(currentCors, newCors)){
            return Move.STRAIGHT;
        }
        if(isDiagonal(currentCors, newCors)){
            return Move.DIAGONAL;
        }
        return Move.INVALID;
    }

    /*
     * purpose: find out if King is in check
     * input: team, white or black
     * result: checks King and finds out if is in check
     */
    public boolean isInCheck(Team t){
        threats.clear();
        int[] king = t.equals(Team.WHITE) ? kingW : kingB;
        int i, j;
        for(i = 0; i < LENGTH; i++){
            for(j = 0; j < WIDTH; j++){
                if(pieces[i][j] != null && canMove(new int[]{i, j}, king, false)){
                    addThreat(new int[]{i, j}, king);
                }
            }
        }
        return threats.size() > 0;
    }

    /*
     * purpose: find out what threat is on King
     * input: int[] enemyCors, int[] kingCors
     * result: enemy and type of threat are added to threats
     */
    public void addThreat(int[] enemyCors, int[] kingCors){
        Move move = typeOfMove(enemyCors, kingCors);
        threats.add(getPath(enemyCors, kingCors, move));
    }

    /*
     * purpose: create path
     * input: enemyCors, currentCors
     * result: list of coordinates on threat path
     */
    public int[][] getPath(int[] currentCors, int[] enemyCors, Move move){
        int[][] path;
        int xDifference = currentCors[0] - enemyCors[0];
        int yDifference = currentCors[1] - enemyCors[1];
        int i = currentCors[0];
        int j = currentCors[1];
        int index = 0;

        if(move.equals(Move.KNIGHT)){
            return new int[][] {currentCors};
        }
        else if(move.equals(Move.STRAIGHT)){
            int size = xDifference != 0 ? Math.abs(xDifference) : Math.abs(yDifference);
            path = new int[size][2];
            if(xDifference < 0){
                while(i < enemyCors[0]){
                    path[index] = new int[]{i, currentCors[1]};
                    i++;
                    index++;
                }
            }
            else if(xDifference > 0){
                while(i > enemyCors[0]){
                    path[index] = new int[]{i, currentCors[1]};
                    i--;
                    index++;
                }
            }
            else if(yDifference < 0){
                while(j < enemyCors[1]){
                    path[index] = new int[]{currentCors[0], j};
                    j++;
                    index++;
                }
            }
            else if(yDifference > 0){
                while(j > enemyCors[1]){
                    path[index] = new int[]{currentCors[0], j};
                    j--;
                    index++;
                }
            }
            return path;
        }
        else if(move.equals(Move.DIAGONAL)){
            path = new int[Math.abs(xDifference)][2];
            if(xDifference > 0 && yDifference > 0){
                while(i > enemyCors[0]){
                    path[index] = new int[]{i, j};
                    i--;
                    j--;
                    index++;
                }
            }
            else if(xDifference > 0 && yDifference < 0){
                while(i > enemyCors[0]){
                    path[index] = new int[]{i, j};
                    i--;
                    j++;
                    index++;
                }
            }
            else if(xDifference < 0 && yDifference > 0){
                while(i < enemyCors[0]){
                    path[index]= new int[]{i, j};
                    i++;
                    j--;
                    index++;
                }
            }
            else if(xDifference < 0 && yDifference < 0){
                while(i < enemyCors[0]){
                    path[index] = new int[]{i, j};
                    i++;
                    j++;
                    index++;
                }
            }
            return path;
        }
        return new int[0][0];
    }

    /*
     * purpose: find out if a checkmate has happened (call at start of turn)
     * input: none
     * result: either checkmate or not
     */
    public boolean isCheckmate(){
        int[] king = turn.equals(Team.WHITE) ? kingW : kingB;
        List<int[]> escapes = kingEscapes(king);
        if(!isInCheck(turn)){
            return false;
        }
        int i, j, k, l;
        for(i = 0; i < escapes.size(); i++){
            for(j = 0; j < threats.size(); j++){
                if(canMove(threats.get(j)[0], escapes.get(i), false)){
                    break;
                }
                else if(i == escapes.size() - 1){
                    return false;
                }
            }
        }
        if(threats.size() > 1){
            return true;
        }
        for(i = 0; i < threats.get(0).length; i++){
            for(j = 0; j < LENGTH; j++){
                for(k = 0; k < WIDTH; k++){
                    for(l = 0; l < threats.get(i).length; l++){
                        if(canMove(new int[]{j, k}, threats.get(i)[l], false)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /*
     * purpose: find out which moves King can make
     * input: cors of current king
     * result: list of cors king can move to
     */
    public List<int[]> kingEscapes(int[] king){
        List<int[]> escapes = new ArrayList<>();
        int i, j;
        for(i = 0; i < LENGTH; i++){
            for(j = 0; j < WIDTH; j++){
                if(Math.abs(i - king[0]) <= 1 && Math.abs(j - king[1]) <= 1 && canMove(king, new int[]{i, j}, false)){
                    escapes.add(new int[]{i, j});
                }
            }
        }
        return escapes;
    }

    /*
     * purpose: convert coordinates into pieces
     * input: between
     * result: array of Pieces
     */
    public Piece[] getBetweenPieces(int[][] between){
        Piece[] betweenPieces = new Piece[between.length];
        int i;
        for(i = 0; i < between.length; i++){
            betweenPieces[i] = pieces[between[i][0]][between[i][1]];
        }
        return betweenPieces;
    }

    /*
     * purpose: Display board tiles/pieces in appropriate locations
     */
    public void paint(Graphics g){
        int i, j;
        int xAxis = 0;
        int yAxis = 0;
        Color c = Color.WHITE;
        for(i = 0; i < LENGTH; i++){
            for(j = 0; j < WIDTH; j++){
                c = c.equals(Color.WHITE) ? Color.GRAY : Color.WHITE;
                g.setColor(c);
                g.fillRect(xAxis, yAxis, 100, 100);
                if(pieces[i][j] != null){
                    g.drawImage(pieces[i][j].getImage(), xAxis + 15, yAxis + 15, null);
                }
                xAxis += 100;
            }
            c = c.equals(Color.WHITE) ? Color.GRAY : Color.WHITE;
            yAxis += 100;
            xAxis = 0;
        }
    }

    /*
     * purpose: turn the click coordinates into coordinates the board can use
     * input: click coordinates
     * result: board coordinates
     */
    public int[] getBoardCoordinates(int[] clickCors){
        return new int[]{clickCors[0] / 100, clickCors[1] / 100};
    }

    public static void main(String[] args) throws InterruptedException {
        // Creating Board
        Board b = new Board();
        ChessListener ml = new ChessListener();
        b.addMouseListener(ml);
        // Creating JFrame
        JFrame frame = new JFrame();
        frame.addMouseListener(ml);
        frame.getContentPane().add(b);
        frame.getContentPane().addMouseListener(ml);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        int[] currentCors, newCors;
        boolean playing = true;
        while(playing){
            b.repaint();
            if(ml.isFirstClick){
                ml.setFirstClick();
            }
            while(!ml.isFirstClick()){
                Thread.sleep(10);
            }
            currentCors = b.getBoardCoordinates(ml.getClickCors());
            Thread.sleep(10);
            while(ml.isFirstClick()){
                Thread.sleep(10);
            }
            newCors = b.getBoardCoordinates(ml.getClickCors());
            System.out.println(currentCors[0] + " " + currentCors[1]);
            System.out.println(newCors[0] + " " + newCors[1]);
            b.move(currentCors, newCors);
            playing = !b.isCheckmate();
        }
    }
}

