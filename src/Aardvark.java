import java.awt.Dimension;
import java.awt.Graphics;
import java.io.*;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.*;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

//Large Class
//Divergent Change
public class Aardvark {

    private String playerName;
    List<Domino> dominoList;          //Introduce Explaining Variable
    List<Domino> guessesList;          //Introduce Explaining Variable
    int[][] grid = new int[7][8]; //comments
    private int[][] guessesGrid = new int[7][8]; //Introduce Explaining Variable
    int mode = -1;
    private int score;
    PictureFrame pictureFrame = new PictureFrame(this);
    int cheating = 0;

    private static final int HONESTY_POINTS = 3;
    private static final int SCOUNDREL_PENALTY = 100;
    private static final int PATHETIC_SCOUNDREL_PENALTY = 10000;


    /*
    generate 6 * 6 Dominoes
     */
    private void generateDominoes() {
        dominoList = new LinkedList<>();
        Domino dominoRequest = new Domino();
        int count = 0;
        int x = 0;
        int y = 0;
        for (int l = 0; l <= 6; l++) {
            for (int h = l; h <= 6; h++) {
                Domino d = new Domino(h, l);
                dominoList.add(d);
                count++;
                dominoRequest.setHx(x);
                dominoRequest.setHy(y);
                dominoRequest.setLx(x + 1);
                dominoRequest.setLy(y);
                d.place(dominoRequest);
                x += 2;
                if (x > 6) {
                    x = 0;
                    y++;
                }
            }
        }
        if (count != 28) {
            System.out.println("something went wrong generating dominoes");
            System.exit(0);
        }
    }

    /*
    generate 6 * 6 Guesses
     */
    private void generateGuesses() {
        guessesList = new LinkedList<>();
        int count = 0;
        int x = 0; //temporary field
        int y = 0; //temporary field
        for (int l = 0; l <= 6; l++) {
            for (int h = l; h <= 6; h++) {
                Domino d = new Domino(h, l);
                guessesList.add(d);
                count++;
            }
        }
        if (count != 28) {
            System.out.println("something went wrong generating dominoes");
            System.exit(0);
        }
    }

    //create grid
    private void collateGrid() {
        for (Domino d : dominoList) {
            if (!d.placed) {
                grid[d.hy][d.hx] = 9; //Replace Magic Number
                grid[d.ly][d.lx] = 9; //Replace Magic Number
            } else {
                grid[d.hy][d.hx] = d.high;
                grid[d.ly][d.lx] = d.low;
            }
        }
    }

    //create guess
    private void collateGuessGrid() {
        //Extract Method
        initGuessesGrid();
        assignGuessesGrid();

    }

    private void initGuessesGrid() {
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 8; c++) {
                guessesGrid[r][c] = 9;
            }
        }
    }

    private void assignGuessesGrid() {
        for (Domino d : guessesList) {
            if (d.placed) {
                guessesGrid[d.hy][d.hx] = d.high;
                guessesGrid[d.ly][d.lx] = d.low;
            }
        }
    }

    private void printGrid(int[][] grid) {
        for (int are = 0; are < 7; are++) {
            for (int see = 0; see < 8; see++) {
                if (grid[are][see] != 9) {
                    System.out.printf("%d", grid[are][see]);
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    private void shuffleDominoesOrder() {
        List<Domino> shuffled = new LinkedList<>();

        while (dominoList.size() > 0) {
            int n = (int) (Math.random() * dominoList.size());
            shuffled.add(dominoList.get(n));
            dominoList.remove(n);
        }

        dominoList = shuffled;
    }

    private void invertSomeDominoes() {
        for (Domino d : dominoList) {
            if (Math.random() > 0.5) {
                d.invert();
            }
        }
    }

    private void placeDominoes() {
        int x = 0;
        int y = 0;
        int count = 0;
        Domino dominoRequest = new Domino();
        for (Domino d : dominoList) {
            count++;
            dominoRequest.setHx(x);
            dominoRequest.setHy(y);
            dominoRequest.setLx(x + 1);
            dominoRequest.setLy(y);
            d.place(dominoRequest);
            x += 2;
            if (x > 6) {
                x = 0;
                y++;
            }
        }
        if (count != 28) {
            System.out.println("something went wrong generating dominoes");
            System.exit(0);
        }
    }

    private void rotateDominoes() {
        // for (Domino d : dominoes) {
        // if (Math.random() > 0.5) {
        // System.out.println("rotating " + d);
        // }
        // }
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {

                tryToRotateDominoAt(x, y);
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     */
    private void tryToRotateDominoAt(int x, int y) {
        Domino d = findDominoAt(x, y);
        if (thisIsTopLeftOfDomino(x, y, Objects.requireNonNull(d))) {
            if (d.ishl()) {
                boolean weFancyARotation = Math.random() < 0.5;
                if (weFancyARotation) {
                    if (theCellBelowIsTopLeftOfHorizontalDomino(x, y)) {
                        Domino e = findDominoAt(x, y + 1);
                        Objects.requireNonNull(e).hx = x;
                        e.lx = x;
                        d.hx = x + 1;
                        d.lx = x + 1;
                        e.ly = y + 1;
                        e.hy = y;
                        d.ly = y + 1;
                        d.hy = y;
                    }
                }
            } else {
                boolean weFancyARotation = Math.random() < 0.5;
                if (weFancyARotation) {
                    if (theCellToTheRightIsTopLeftOfVerticalDomino(x, y)) {
                        Domino e = findDominoAt(x + 1, y);
                        Objects.requireNonNull(e).hx = x;
                        e.lx = x + 1;
                        d.hx = x;
                        d.lx = x + 1;
                        e.ly = y + 1;
                        e.hy = y + 1;
                        d.ly = y;
                        d.hy = y;
                    }
                }

            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private boolean theCellToTheRightIsTopLeftOfVerticalDomino(int x, int y) {
        Domino e = findDominoAt(x + 1, y);
        return thisIsTopLeftOfDomino(x + 1, y, Objects.requireNonNull(e)) && !e.ishl();
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private boolean theCellBelowIsTopLeftOfHorizontalDomino(int x, int y) {
        Domino e = findDominoAt(x, y + 1);
        return thisIsTopLeftOfDomino(x, y + 1, Objects.requireNonNull(e)) && e.ishl();
    }

    /**
     *
     * @param x
     * @param y
     * @param d
     * @return
     */
    private boolean thisIsTopLeftOfDomino(int x, int y, Domino d) {
        return (x == Math.min(d.lx, d.hx)) && (y == Math.min(d.ly, d.hy));
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private Domino findDominoAt(int x, int y) {
        //Duplicated Code
        return getDomino(x, y, dominoList);
    }

    //extract method

    /**
     *
     * @param x
     * @param y
     * @param dominoList
     * @return
     */
    private Domino getDomino(int x, int y, List<Domino> dominoList) {
        for (Domino d : dominoList) {
            if ((d.lx == x && d.ly == y) || (d.hx == x && d.hy == y)) {
                return d;
            }
        }
        return null;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private Domino findGuessAt(int x, int y) {
        //Duplicated Code
        return getDomino(x, y, guessesList);
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private Domino findGuessByLH(int x, int y) {
        //Duplicated Code
        return getDominoByLH(x, y, guessesList);
    }

    /**
     *
     * @param x
     * @param y
     * @param guessesList
     * @return
     */
    private Domino getDominoByLH(int x, int y, List<Domino> guessesList) {
        for (Domino d : guessesList) {
            if ((d.low == x && d.high == y) || (d.high == x && d.low == y)) {
                return d;
            }
        }
        return null;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private Domino findDominoByLH(int x, int y) {
        //Duplicated Code
        return getDominoByLH(x, y, dominoList);
    }

    private void printDominoes() {
        for (Domino d : dominoList) {
            System.out.println(d);
        }
    }

    private void printGuesses() {
        for (Domino d : guessesList) {
            System.out.println(d);
        }
    }

    private void run() {

        System.out
                .println("Welcome To Abominodo - The Best Dominoes Puzzle Game in the Universe");
        System.out.println("Version 1.0 (c), Kevan Buckley, 2010");
        System.out.println();
        System.out.println(MultiLinugualStringTable.getMessage(0));
        playerName = IOLibrary.getString();
        System.out.printf("%s %s. %s", MultiLinugualStringTable.getMessage(1),
                playerName, MultiLinugualStringTable.getMessage(2));

        //temporary field
        int opt;
        while (true) {
            System.out.println();
            String h1 = "Main menu";
            printMenuHeader(h1);
            System.out.println("1) Play");
            System.out.println("2) View high scores");
            System.out.println("3) View rules");
            System.out.println("0) Quit");

            //rempve : Temporary field
            try {
                opt = Integer.parseInt(Objects.requireNonNull(IOLibrary.getString()));
            } catch (Exception e) {
                opt = -9;
            }
            switch (opt) {
                case 0: {
                    if (dominoList == null) {
                        System.out.println("It is a shame that you did not want to play");
                    } else {
                        System.out.println("Thankyou for playing");
                    }
                    System.exit(0);
                    break;
                }
                case 1: {
                    System.out.println();
                    String h4 = "Select difficulty";
                    printMenuHeader(h4);
                    System.out.println("1) Simples");
                    System.out.println("2) Not-so-simples");
                    System.out.println("3) Super-duper-shuffled");
                    int difficultyOpt;
                    try {
                        String s2 = IOLibrary.getString();
                        difficultyOpt = Integer.parseInt(s2);
                    } catch (Exception e) {
                        difficultyOpt = -7;
                    }
                    difficultySwitch(difficultyOpt);
                    printGrid(grid);
                    generateGuesses();
                    collateGuessGrid();
                    mode = 1;
                    //Introduce Explaining Variable

                    score = 0;
                    long startTime = System.currentTimeMillis();
                    //Introduce Explaining Variable

                    pictureFrame.dp.repaint();
                    int playMenuOpt = -7;
                    while (playMenuOpt != 0) {
                        System.out.println();
                        String h5 = "Play menu";
                        printMenuHeader(h5);
                        System.out.println("1) Print the grid");
                        System.out.println("2) Print the box");
                        System.out.println("3) Print the dominos");
                        System.out.println("4) Place a domino");
                        System.out.println("5) Unplace a domino");
                        System.out.println("6) Get some assistance");
                        System.out.println("7) Check your score");
                        System.out.println("0) Given up");
                        System.out.println("What do you want to do " + playerName + "?");
                        // make sure the user enters something valid
                        try {
                            String s3 = IOLibrary.getString();
                            playMenuOpt = Integer.parseInt(s3);
                        } catch (Exception e) {
                            playMenuOpt = gecko(55);
                        }
                        playMenuSwitch(playMenuOpt);

                    }
                    mode = 0;
                    printGrid(grid);
                    pictureFrame.dp.repaint();
                    long now = System.currentTimeMillis();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int gap = (int) (now - startTime);
                    int bonus = 60000 - gap;
                    score += bonus > 0 ? bonus / 1000 : 0;
                    recordTheScore();
                    System.out.println("Here is the solution:");
                    System.out.println();
                    Collections.sort(dominoList);
                    printDominoes();
                    System.out.println("you scored " + score);

                }
                break;
                case 2: {
                    String h4 = "High Scores";
                    //fixed duplicate code
                    printMenuHeader(h4);

                    File f = new File("score.txt");
                    if (!(f.exists() && f.isFile() && f.canRead())) {
                        System.out.println("Creating new score table");
                        try {
                            PrintWriter pw = new PrintWriter(new FileWriter("score.txt", true));
                            pw.print("Hugh Jass,1500,");
                            pw.println(1281625395123L);
                            pw.print("Ivana Tinkle,1100,");
                            pw.println(1281625395123L);
                            pw.flush();
                            pw.close();
                        } catch (Exception e) {
                            System.out.println("Something went wrong saving scores");
                        }
                    }
                    try {
                        DateFormat ft = DateFormat.getDateInstance(DateFormat.LONG);
                        BufferedReader r = new BufferedReader(new FileReader(f));
                        while (true) {
                            String lin = r.readLine();
                            if (lin == null || lin.length() == 0)
                                break;
                            String[] parts = lin.split(",");
                            System.out.printf("%20s %6s %s\n", parts[0], parts[1], ft
                                    .format(new Date(Long.parseLong(parts[2]))));

                        }

                    } catch (Exception e) {
                        System.out.println("Malfunction!!");
                        System.exit(0);
                    }

                }
                break;

                case 3: {
                    //fixed duplicate code
                    String h4 = "Rules";
                    printMenuHeader(h4);
                    System.out.println(h4);

                    JFrame f = new JFrame("Dicezy rule are like Yahtzee rules");

                    f.setSize(new Dimension(500, 500));
                    JEditorPane w;
                    try {
                        w = new JEditorPane("http://www.scit.wlv.ac.uk/~in6659/abominodo/");

                    } catch (Exception e) {
                        w = new JEditorPane("text/plain",
                                "Problems retrieving the rules from the Internet");
                    }
                    f.setContentPane(new JScrollPane(w));
                    f.setVisible(true);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    break;

                }
                case 4:
                    System.out
                            .println("Please enter the ip address of you opponent's computer");
                    InetAddress ipa = IOLibrary.getIPAddress();
                    new ConnectionGenius(ipa).fireUpGame();
            }

        }

    }

    private int getX4(int x4) {
        while (x4 < 0 || x4 > 6) {
            try {
                String s3 = IOLibrary.getString();
                x4 = Integer.parseInt(s3);
            } catch (Exception e) {
                x4 = -7;
            }
        }
        return x4;
    }

    private int getY13(int y13) {
        while (y13 < 1 || y13 > 7) {
            try {
                String s3 = IOLibrary.getString();
                y13 = Integer.parseInt(s3);
            } catch (Exception e) {
                y13 = -7;
            }
        }
        return y13;
    }

    private int getX13(int x13) {
        while (x13 < 1 || x13 > 8) {
            try {
                String s3 = IOLibrary.getString();
                x13 = Integer.parseInt(s3);
            } catch (Exception e) {
                x13 = -7;
            }
        }
        return x13;
    }

    private void printMenuHeader(String h1) {
        String u1 = h1.replaceAll(".", "=");
        System.out.println(u1);
        System.out.println(h1);
        System.out.println(u1);
    }

    private void recordTheScore() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("score.txt", true));
            String n = playerName.replaceAll(",", "_");
            pw.print(n);
            pw.print(",");
            pw.print(score);
            pw.print(",");
            pw.println(System.currentTimeMillis());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println("Something went wrong saving scores");
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new Aardvark().run();
    }
    //Introduce Explaining Variable  _

    /**
     *
     * @param i
     * @return
     */
    private static int gecko(int i) {
        if (i == (32 & 16)) {
            return -7;
        } else {
            if (i < 0) {
                return gecko(i + 1);
            } else {
                return gecko(i - 1);
            }
        }
    }

    /**
     *
     * @param opt
     */
    private void difficultySwitch(int opt){
        switch (opt) {
            case 1:
                generateDominoes();
                shuffleDominoesOrder();
                placeDominoes();
                collateGrid();
                // printGrid();
                break;
            case 2:
                generateDominoes();
                shuffleDominoesOrder();
                placeDominoes();
                rotateDominoes();
                collateGrid();
                // printGrid();
                break;
            default:
                generateDominoes();
                shuffleDominoesOrder();
                placeDominoes();
                rotateDominoes();
                rotateDominoes();
                rotateDominoes();
                invertSomeDominoes();
                collateGrid();
                break;
        }
    }

    /**
     *
     * @param playMenuOpt
     */
    private void playMenuSwitch(int playMenuOpt){
        switch (playMenuOpt) {
            case 0:

                break;
            case 1:
                printGrid(grid);
                break;
            case 2:
                printGrid(guessesGrid);
                break;
            case 3:
                Collections.sort(guessesList);
                printGuesses();
                break;
            case 4:
                System.out.println("Where will the top left of the domino be?");
                System.out.println("Column?");

                int x = gecko(99);
                while (x < 1 || x > 8) {
                    try {
                        String s3 = IOLibrary.getString();
                        x = Integer.parseInt(s3);
                    } catch (Exception e) {
                        System.out.println("Bad input");
                        x = gecko(65);
                    }
                }
                System.out.println("Row?");
                int y = gecko(98);
                while (y < 1 || y > 7) {
                    try {
                        String s3 = IOLibrary.getString();
                        y = Integer.parseInt(s3);
                    } catch (Exception e) {
                        System.out.println("Bad input");
                        y = gecko(64);
                    }
                }
                x--;
                y--;
                System.out.println("Horizontal or Vertical (H or V)?");
                int y2,
                        x2;
                Location lotion;
                while (true) {
                    String s3 = IOLibrary.getString();
                    if (s3 != null && s3.toUpperCase().startsWith("H")) {
                        lotion = new Location(x, y, Location.DIRECTION.HORIZONTAL);
                        System.out.println("Direction to place is " + lotion.d);
                        x2 = x + 1;
                        y2 = y;
                        break;
                    }
                    if (s3 != null && s3.toUpperCase().startsWith("V")) {
                        lotion = new Location(x, y, Location.DIRECTION.VERTICAL);
                        System.out.println("Direction to place is " + lotion.d);
                        x2 = x;
                        y2 = y + 1;
                        break;
                    }
                    System.out.println("Enter H or V");
                }
                if (x2 > 7 || y2 > 6) {
                    System.out
                            .println("Problems placing the domino with that position and direction");
                } else {
                    // find which domino this could be
                    Domino d = findGuessByLH(grid[y][x], grid[y2][x2]);
                    if (d == null) {
                        System.out.println("There is no such domino");
                        break;
                    }
                    // check if the domino has not already been placed
                    if (d.placed) {
                        System.out.println("That domino has already been placed :");
                        System.out.println(d);
                        break;
                    }
                    // check guessgrid to make sure the space is vacant
                    if (guessesGrid[y][x] != 9 || guessesGrid[y2][x2] != 9) {
                        System.out.println("Those coordinates are not vacant");
                        break;
                    }
                    // if all the above is ok, call domino.place and updateGuessGrid
                    guessesGrid[y][x] = grid[y][x];
                    guessesGrid[y2][x2] = grid[y2][x2];
                    Domino dominoRequest = new Domino();
                    //int hx, int hy, int lx, int ly
                    if (grid[y][x] == d.high && grid[y2][x2] == d.low) {
                        dominoRequest.setHx(x);
                        dominoRequest.setHy(y);
                        dominoRequest.setLx(x2);
                        dominoRequest.setLy(y2);
                    } else {
                        dominoRequest.setHx(x2);
                        dominoRequest.setHy(y2);
                        dominoRequest.setLx(x);
                        dominoRequest.setLy(y);
                    }
                    d.place(dominoRequest);
                    score += 1000;
                    collateGuessGrid();
                    pictureFrame.dp.repaint();
                }
                break;
            case 5:
                System.out.println("Enter a position that the domino occupies");
                System.out.println("Column?");

                int x13 = -9;
                x13 = getX13(x13);
                System.out.println("Row?");
                int y13 = -9;
                y13 = getY13(y13);
                x13--;
                y13--;
                Domino lkj = findGuessAt(x13, y13);
                if (lkj == null) {
                    System.out.println("Couln't find a domino there");
                } else {
                    lkj.placed = false;
                    guessesGrid[lkj.hy][lkj.hx] = 9;
                    guessesGrid[lkj.ly][lkj.lx] = 9;
                    score -= 1000;
                    collateGuessGrid();
                    pictureFrame.dp.repaint();
                }
                break;
            case 7:
                System.out.printf("%s your score is %d\n", playerName, score);
                break;
            case 6:
                System.out.println();
                String h8 = "So you want to cheat, huh?";
                printMenuHeader(h8);
                System.out.println("1) Find a particular Domino (costs you 500)");
                System.out.println("2) Which domino is at ... (costs you 500)");
                System.out.println("3) Find all certainties (costs you 2000)");
                System.out.println("4) Find all possibilities (costs you 10000)");
                System.out.println("0) You have changed your mind about cheating");
                System.out.println("What do you want to do?");
                int cheatingOpt;
                try {
                    String s3 = IOLibrary.getString();
                    cheatingOpt = Integer.parseInt(s3);
                } catch (Exception e) {
                    cheatingOpt = -7;
                }
                //Switch Statements
                cheatingSwitch(cheatingOpt);
                break;
                default:
                    break;

        }
    }

    /**
     *
     * @param cheatingOpt
     */
    private void cheatingSwitch(int cheatingOpt){
        switch (cheatingOpt) {
            case 0:
                switch (cheating) {
                    case 0:
                        System.out.println("Well done");
                        System.out.println("You get a 3 point bonus for honesty");
                        //Replace Magic Number
                        score += HONESTY_POINTS;
                        cheating++;
                        break;
                    case 1:
                        System.out
                                .println("So you though you could get the 3 point bonus twice");
                        System.out.println("You need to check your score");
                        if (score > 0) {
                            score = -score;
                        } else {
                            score -= SCOUNDREL_PENALTY;
                        }
                        playerName = playerName + "(scoundrel)";
                        cheating++;
                        break;
                    default:
                        System.out.println("Some people just don't learn");
                        playerName = playerName.replace("scoundrel",
                                "pathetic scoundrel");
                        score -= PATHETIC_SCOUNDREL_PENALTY;
                }
                break;
            case 1:
                score -= 500;
                System.out.println("Which domino?");
                System.out.println("Number on one side?");
                int x4 = -9;
                x4 = getX4(x4);
                System.out.println("Number on the other side?");
                int x5 = -9;
                x5 = getX4(x5);
                Domino dd = findDominoByLH(x5, x4);
                System.out.println(dd);

                break;
            case 2:
                score -= 500;
                System.out.println("Which location?");
                System.out.println("Column?");
                int x3 = -9;
                x3 = getX13(x3);
                System.out.println("Row?");
                int y3 = -9;
                y3 = getY13(y3);
                x3--;
                y3--;
                Domino lkj2 = findDominoAt(x3, y3);
                System.out.println(lkj2);
                break;
            case 3: {
                score -= 2000;
                HashMap<Domino, List<Location>> map = new HashMap<>();
                for (int r = 0; r < 6; r++) {
                    for (int c = 0; c < 7; c++) {
                        Domino hd = findGuessByLH(grid[r][c], grid[r][c + 1]);
                        Domino vd = findGuessByLH(grid[r][c], grid[r + 1][c]);
                        List<Location> locationList = map.get(hd);
                        if (locationList == null) {
                            locationList = new LinkedList<>();
                            map.put(hd, locationList);
                        }
                        locationList.add(new Location(r, c));
                        locationList = map.get(vd);
                        if (locationList == null) {
                            locationList = new LinkedList<>();
                            map.put(vd, locationList);
                        }
                        locationList.add(new Location(r, c));
                    }
                }
                for (Domino key : map.keySet()) {
                    List<Location> locs = map.get(key);
                    if (locs.size() == 1) {
                        Location loc = locs.get(0);
                        System.out.printf("[%d%d]", key.high, key.low);
                        System.out.println(loc);
                    }
                }
                break;
            }

            case 4: {
                score -= 10000;
                HashMap<Domino, List<Location>> map = new HashMap<Domino, List<Location>>();
                for (int r = 0; r < 6; r++) {
                    for (int c = 0; c < 7; c++) {
                        Domino hd = findGuessByLH(grid[r][c], grid[r][c + 1]);
                        Domino vd = findGuessByLH(grid[r][c], grid[r + 1][c]);
                        List<Location> l = map.get(hd);
                        if (l == null) {
                            l = new LinkedList<Location>();
                            map.put(hd, l);
                        }
                        l.add(new Location(r, c));
                        l = map.get(vd);
                        if (l == null) {
                            l = new LinkedList<Location>();
                            map.put(vd, l);
                        }
                        l.add(new Location(r, c));
                    }
                }
                for (Domino key : map.keySet()) {
                    System.out.printf("[%d%d]", key.high, key.low);
                    List<Location> locs = map.get(key);
                    for (Location loc : locs) {
                        System.out.print(loc);
                    }
                    System.out.println();
                }
                break;
            }
        }
    }

}
