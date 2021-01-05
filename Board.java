import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Board extends JPanel {
    protected Piece[][] pieces;
    protected List<int[][]> threats;
    protected final int LENGTH = 8;
    protected final int WIDTH = 8;
    protected int[] kingW, kingB;
    protected Team turn;
    protected boolean successfulMove;

    public Board(){
        turn = Team.WHITE;
        pieces = new Piece[LENGTH][WIDTH];
        kingW = new int[]{7, 4};
        kingB = new int[]{0, 4};
        threats = new ArrayList<>();
        successfulMove = true;
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
        Piece[] whiteOrder = {new Rook(w), new Knight(w), new Bishop(w), new Queen(w), new King(w), new Bishop(w),
                                new Knight(w), new Rook(w)};
        pieces[0] = blackOrder;
        int j;
        for(j = 0; j < LENGTH; j++){
            pieces[1][j] = new Pawn(b);
            pieces[6][j] = new Pawn(w);
        }
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
        if(isInBounds(currentCors, newCors) || pieces[currentCors[0]][currentCors[1]] == null){
            return false;
        }
        List<int[]> possibleMoves = pieces[currentCors[0]][currentCors[1]].getMoves(pieces, currentCors, isMoving);
        int i;
        int len = possibleMoves.size();
        for(i = 0; i < len; i++){
            if(Arrays.equals(possibleMoves.get(i), newCors)){
                return true;
            }
        }
        return false;
}

    /*
     * purpose: determine if it is current pieces turn
     * input: cors
     * result: boolean whether is piece's turn
     */
    public boolean isPieceTurn(int[] cors){
        return pieces[cors[0]][cors[1]].isMyTurn(turn);
    }

   /*
    * purpose: determine of cor is a king
    * input: int[] cors
    * result: returns boolean true or false
    */
   public boolean isKing(int[] cors){
       return (Arrays.equals(cors, kingW) || Arrays.equals(cors, kingB));
   }

    /*
     * purpose: if a piece can move, move it
     * input: currentCors, newCors
     * result: Piece is moved to a new Square
     */
    public boolean move(int[] currentCors, int[] newCors){
        if(canMove(currentCors, newCors, true) && !isKing(newCors) && isPieceTurn(currentCors)){
            if(isKing(currentCors)){
                kingW = turn.equals(Team.WHITE) ? newCors : kingW;
                kingB = turn.equals(Team.WHITE) ? kingB : newCors;
            }
            Piece temp = pieces[newCors[0]][newCors[1]];
            pieces[newCors[0]][newCors[1]] = pieces[currentCors[0]][currentCors[1]];
            pieces[currentCors[0]][currentCors[1]] = null;
            if(isInCheck(turn)){
                System.out.println("invalid move");
                if(isKing(newCors)){
                    kingW = turn.equals(Team.WHITE) ? currentCors : kingW;
                    kingB = turn.equals(Team.WHITE) ? kingB : currentCors;
                }
                pieces[currentCors[0]][currentCors[1]] = pieces[newCors[0]][newCors[1]];
                pieces[newCors[0]][newCors[1]] = temp;
                this.successfulMove = false;
                return false;
            }
            turn = turn.equals(Team.WHITE) ? Team.BLACK : Team.WHITE;
            System.out.println("valid move");
            this.successfulMove = true;
            return true;
        }
        System.out.println("invalid move");
        this.successfulMove = false;
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
        boolean canMove;
        for(i = 0; i < LENGTH; i++){
            for(j = 0; j < WIDTH; j++){
                canMove = canMove(new int[]{i, j}, king, false);
                if(pieces[i][j] != null && canMove && pieces[i][j].canCapture(new Pawn(turn))){
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
        int i;
        for(i = 0; i < escapes.size(); i++){
            if(!wouldBeCheck(escapes.get(i))){
                return false;
            }
        }
        if(threats.size() > 1){
            return true;
        }
        int j, k;
        boolean canMove;
        for(i = 0; i < threats.get(0).length; i++){
            for(j = 0; j < WIDTH; j++){
                for(k = 0; k < LENGTH; k++){
                    canMove = canMove(new int[]{j, k}, threats.get(0)[i], false);
                    if(pieces[j][k] != null && canMove && !pieces[j][k].canCapture(new Pawn(turn))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
     * purpose: determine if it would be check if king occupied given cors
     * input: int[] cors
     * result: boolean true/false
     */
    public boolean wouldBeCheck(int[] cors){
        int i, j;
        for(i = 0; i < LENGTH; i++){
            for(j = 0; j < WIDTH; j++){
                if(canMove(new int[]{i, j}, cors, false) && pieces[i][j].canCapture(new Pawn(turn))){
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * purpose: find out which moves King can make
     * input: cors of current king
     * result: list of cors king can move to
     */
    public List<int[]> kingEscapes(int[] king){
        List<int[]> escapes = new ArrayList<>();
        int i, j;
        boolean canMove;
        for(i = 0; i < LENGTH; i++){
            for(j = 0; j < WIDTH; j++){
                canMove = canMove(king, new int[]{i, j}, false);
                if(Math.abs(i - king[0]) <= 1 && Math.abs(j - king[1]) <= 1 && canMove){
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
     * purpose: get pieces for use by cpu
     * result: return pieces
     */
    public Piece[][] getPieces(){
        return this.pieces;
    }

    /*
     * purpose: Display board tiles/pieces in appropriate locations
     */
    public void paint(Graphics g){
        int i, j;
        int xAxis = 468;
        int yAxis = 150;
        g.drawRect(xAxis, yAxis, 600, 600);
        Color c = Color.WHITE;
        Color d = new Color(173, 216, 230);
        for(i = 0; i < LENGTH; i++){
            for(j = 0; j < WIDTH; j++){
                c = c.equals(Color.WHITE) ? d : Color.WHITE;
                g.setColor(c);
                g.fillRect(xAxis, yAxis, 75, 75);
                if(pieces[i][j] != null){
                    g.drawImage(pieces[i][j].getImage(), xAxis + 5, yAxis + 5, null);
                }
                xAxis += 75;
            }
            c = c.equals(Color.WHITE) ? d : Color.WHITE;
            yAxis += 75;
            xAxis = 468;
        }
        c = d;
        g.fillRect(618, 50, 300, 50);
        String playerTurn = turn.equals(Team.WHITE) ? "White's" : "Black's";
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.setColor(Color.BLACK);
        g.drawRect(618, 50, 300, 50);
        if(!successfulMove){
            g.drawString("Invalid Move", 670, 80);
        }
        else{
            g.drawString("It is " + playerTurn + " turn!", 670, 80);
        }
    }

    /*
     * purpose: turn the click coordinates into coordinates the board can use
     * input: click coordinates
     * result: board coordinates
     */
    public int[] getBoardCoordinates(int[] clickCors){
        return new int[]{(clickCors[0] - 150) / 75, (clickCors[1] - 468) / 75 };
    }

    /*
     * purpose: write text indicating successful/not successful move
     * result: desired text drawn on screen
     */
    public void drawText(String text){
        Graphics g = getGraphics();
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        Color d = new Color(173, 216, 230);
        g.setColor(d);
        g.fillRect(619, 51, 299, 49);
        g.setColor(Color.BLACK);
        g.drawString(text, 670, 80);
    }

    /*
     * purpose: give CPU enemy king cors
     * input: Team representing CPU's team
     * return cors of his enemy king
     */
    public int[] enemyKing(Team t){
        return t.equals(Team.WHITE) ? kingB : kingW;
    }

    /*
     * purpose: create JFrame
     */
    public void getJFrame(ChessListener ml){
        JFrame frame = new JFrame();
        frame.addMouseListener(ml);
        frame.getContentPane().add(this);
        frame.getContentPane().addMouseListener(ml);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(new Color(222, 184, 135));
        frame.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        // Creating Board
        Board b = new Board();
        ChessListener ml = new ChessListener();
        b.addMouseListener(ml);
        // Creating JFrame
        b.getJFrame(ml);
        boolean playing = true;
        CPU cpu = new CPU(Team.BLACK, b);
        int[] currentCors, newCors;
        while(playing){
            b.repaint();
            if(b.turn.equals(cpu.team)){
                while(!cpu.makeMove(b.getPieces())){
                    Thread.sleep(1);
                }
            }
            else{
                while(!ml.isFirstClick()){
                    Thread.sleep(10);
                }
                currentCors = b.getBoardCoordinates(ml.getClickCors());
                while(ml.isFirstClick()){
                    Thread.sleep(10);
                }
                newCors = b.getBoardCoordinates(ml.getClickCors());
                b.move(currentCors, newCors);
            }
            playing = !b.isCheckmate();
        }
    }
}

