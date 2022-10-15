import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MiriSweeper extends PreGame implements ActionListener, MouseListener {

    static final int BUTTON_SIZE = 50;
    static final int PANEL_SIZE = BUTTON_SIZE * SIZE;
    static final int DISPLAYPANEL_SIZE = 60;

    static final int MINES_COUNT = (SIZE*SIZE)/5;
    static final int BOMB = 9;

    static boolean firstInput = true;

    static int[][] playArea = new int[SIZE + 2][SIZE + 2];

    static int[][] isChecked = new int[SIZE][SIZE];

    static int[][] isColored = new int[SIZE][SIZE];

    static int[][] bombExclusionZone = new int[SIZE][SIZE];

    static int[] temp = new int[2];

    static int[][] gameMatrix;

    static int[] input;

    static JButton[][] buttons = new JButton[SIZE+1][SIZE+1];

    static JFrame frame = new JFrame();

    static JLabel label;

    static int flagNum = 0;


    MiriSweeper() {

        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(PANEL_SIZE,PANEL_SIZE + DISPLAYPANEL_SIZE));
        mainPanel.setBackground(Color.black);

        label = new JLabel();
        label.setBounds(0,PANEL_SIZE,PANEL_SIZE,DISPLAYPANEL_SIZE);
        label.setText("Flags Placed: " + flagNum + "/" + MINES_COUNT);
        label.setFont(new Font("Comic Sans MS",Font.BOLD,30));
        label.setForeground(new Color(0xFFFFFF));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(SIZE,SIZE));
        panel.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        panel.setBounds(0,0,PANEL_SIZE,PANEL_SIZE);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("MiriSweeper");

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBounds(BUTTON_SIZE*j,BUTTON_SIZE*i,BUTTON_SIZE,BUTTON_SIZE);
                buttons[i][j].addActionListener(this);
                buttons[i][j].addMouseListener(this);
                buttons[i][j].setBackground(new Color(0xA1A1A1));
                buttons[i][j].setFocusable(false);
                panel.add(buttons[i][j]);
            }
        }
        mainPanel.add(panel);
        mainPanel.add(label);
        frame.add(mainPanel);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);

        gameMatrix = new int[SIZE][SIZE];

        input = new int[2];
        // input[0] for row, input[1] for column

        setPlayArea(playArea);
        System.out.println("SIZE: " + SIZE);
        System.out.println("Bomb count: " + (MINES_COUNT));

    }


    public static void openZeros(int[][] gameMatrix, int[] input) {

        int k = input[0] + 1;
        int l = input[1] + 1;

        if (gameMatrix[input[0]][input[1]] == 0) {
            for (int m = k - 1; m < k + 2; m++) {
                for (int n = l - 1; n < l + 2; n++) {
                    if (playArea[m][n] == 1) {
                        if (gameMatrix[m-1][n-1] == 0) {
                            if (isChecked[m - 1][n - 1] == 0) {
                                isChecked[m - 1][n - 1] = 1;
                                temp[0] = m-1;
                                temp[1] = n-1;
                                openZeros(gameMatrix, temp);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void openNumbers(int[][] gameMatrix){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameMatrix[i][j] == 0 && isChecked[i][j] == 1) {
                    int k = i + 1;
                    int l = j + 1;
                    for (int m = k-1; m < k+2; m++) {
                        for (int n = l-1; n < l+2; n++) {
                            if (playArea[m][n] == 1) {
                                isChecked[m-1][n-1] = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void displayGUI() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isChecked[i][j] == 1) {
                    if (gameMatrix[i][j] != 0) {
                        buttons[i][j].setText("" + gameMatrix[i][j]);
                        buttons[i][j].setFont(new Font("Comic Sans MS",Font.BOLD,15));
                        switch (gameMatrix[i][j]) {
                            case 1 -> buttons[i][j].setForeground(new Color(0x006FFF));
                            case 2 -> buttons[i][j].setForeground(new Color(0x025613));
                            case 3 -> buttons[i][j].setForeground(new Color(0xFF0000));
                            case 4 -> buttons[i][j].setForeground(new Color(0x0004FF));
                            case 5 -> buttons[i][j].setForeground(new Color(0x7E0000));
                            case 6 -> buttons[i][j].setForeground(new Color(0x00FFD2));
                            case 7 -> buttons[i][j].setForeground(new Color(0x000000));
                            case 8 -> buttons[i][j].setForeground(new Color(0x737373));
                        }
                    }
                    buttons[i][j].setBackground(new Color(0xE0E0E0));
                }
            }
        }
    }

    public static void placeBombs(int[][] gameMatrix, int[] input) {
        gameMatrix[input[0]][input[1]] = 0;
        int x;
        int y;

        x = (int)(Math.random() * gameMatrix.length);
        y = (int)(Math.random() * gameMatrix.length);

        int k = input[0] + 1;
        int l = input[1] + 1;

        for (int m = k - 1; m < k + 2; m++) {
            for (int n = l - 1; n < l + 2; n++) {
                if (playArea[m][n] == 1) {
                    bombExclusionZone[m - 1][n - 1] = 1;
                }
            }
        }

        for (int i = 0; i < MINES_COUNT; i++) {
            while (bombExclusionZone[x][y] == 1 || gameMatrix[x][y] == BOMB) {
                x = randomizeValues();
                y = randomizeValues();
            }
            gameMatrix[x][y] = BOMB;
            x = randomizeValues();
            y = randomizeValues();
        }
    }

    public static void evaluateBoard(int[][] gameMatrix) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameMatrix[i][j] == BOMB) {
                    int k = i + 1;
                    int l = j + 1;
                    for (int m = k - 1; m < k + 2; m++) {
                        for (int n = l - 1; n < l + 2; n++) {
                            if (playArea[m][n] == 1) {
                                if (gameMatrix[m-1][n-1] == gameMatrix[i][j])
                                    continue;
                                gameMatrix[m-1][n-1]++;
                            }
                        }
                    }
                }
            }
        }
    }

    public static int randomizeValues() {
        return (int)(Math.random() * SIZE);
    }

    public static void setPlayArea(int[][] playArea) {
        for (int i = 0; i < SIZE + 2; i++) {
            for (int j = 0; j < SIZE + 2; j++) {
                if (i > 0 && j > 0){
                    if (i < SIZE + 1 && j < SIZE + 1) {
                        playArea[i][j] = 1;
                    }
                }
                else {
                    playArea[i][j] = 0;
                }
            }
        }
    }

    // Left Click Actions
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (e.getSource() == buttons[i][j]) {
                    if (isColored[i][j] != 1) {
                        input[0] = i;
                        input[1] = j;
                        if (firstInput) {
                            firstInput = false;
                            placeBombs(gameMatrix, input);
                            evaluateBoard(gameMatrix);
                        }
                        openZeros(gameMatrix,input);
                        openNumbers(gameMatrix);
                        isChecked[i][j] = 1;
                        displayGUI();
                        if (gameMatrix[i][j] == BOMB) {
                            for (int k = 0; k < SIZE; k++) {
                                for (int l = 0; l < SIZE; l++) {
                                    if (gameMatrix[k][l] == BOMB) {
                                        buttons[k][l].setText("B");
                                        buttons[k][l].setBackground(Color.red);
                                    }
                                }
                            }
                            JOptionPane.showMessageDialog(null,"Sorry, You Lost :(.",":(",JOptionPane.PLAIN_MESSAGE);
                            frame.dispose();
                            System.exit(0);
                        }
                    }
                }
            }
        }

        double winNum = Math.pow(SIZE,2)-MINES_COUNT;
        int counter = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isChecked[i][j] == 1 && gameMatrix[i][j] != BOMB) {
                    counter++;
                }
            }
        }
        if (counter == winNum) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (gameMatrix[i][j] == BOMB) {
                        buttons[i][j].setBackground(new Color(0x690000));
                    }
                }
            }
            JOptionPane.showMessageDialog(null,"YOU WIN!! CONGRATS!!.",":)",JOptionPane.PLAIN_MESSAGE);
            frame.dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


    // Right Click Actions
    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getSource() == buttons[i][j]) {
                        if (isChecked[i][j] != 1) {
                            if (isColored[i][j] == 1) {
                                buttons[i][j].setBackground(new Color(0xA1A1A1));
                                flagNum--;
                                isColored[i][j] = 0;
                            } else {
                                buttons[i][j].setBackground(new Color(0x690000));
                                isColored[i][j] = 1;
                                flagNum++;
                            }
                            label.setText("Flags Placed: " + flagNum + "/" + MINES_COUNT);
                        }
                    }
                }
            }
        }
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
