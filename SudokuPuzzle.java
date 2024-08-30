import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SudokuPuzzle extends Frame {
    private static final int SIZE = 9;
    private TextField[][] cells = new TextField[SIZE][SIZE];
    private Button solveButton = new Button("Solve");

    private static final int[][] INITIAL_PUZZLE = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    public SudokuPuzzle() {
        setTitle("Sudoku Solver");
        setSize(400, 400);
        setLayout(new GridBagLayout());
        initializeUI();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose(); 
            }
        });
        setVisible(true);
    }

    private void initializeUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.ipady = 20;

       
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new TextField(2);
                cells[row][col].setFont(new Font("Arial", Font.PLAIN, 18));
                gbc.gridx = col;
                gbc.gridy = row;
                add(cells[row][col], gbc);
            }
        }

       
        populateInitialPuzzle();

  
        gbc.gridx = 0;
        gbc.gridy = SIZE;
        gbc.gridwidth = SIZE;
        solveButton.addActionListener(new ActionListener() {
        
            public void actionPerformed(ActionEvent e) {
                int[][] grid = getGridFromUI();
                if (solveSudoku(grid)) {
                    updateUIWithSolution(grid);
                } else {
                    showMessage("No solution exists.");
                }
            }
        });
        add(solveButton, gbc);
    }

    private void populateInitialPuzzle() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (INITIAL_PUZZLE[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(INITIAL_PUZZLE[row][col]));
                    cells[row][col].setEditable(false); 
                }
            }
        }
    }

    private int[][] getGridFromUI() {
        int[][] grid = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                try {
                    grid[row][col] = Integer.parseInt(cells[row][col].getText());
                } catch (NumberFormatException e) {
                    grid[row][col] = 0; 
                }
            }
        }
        return grid;
    }

    private void updateUIWithSolution(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText(String.valueOf(grid[row][col]));
            }
        }
    }

    private boolean isValid(int[][] grid, int row, int col, int num) {
   
        for (int c = 0; c < SIZE; c++) {
            if (grid[row][c] == num) {
                return false;
            }
        }

      
        for (int r = 0; r < SIZE; r++) {
            if (grid[r][col] == num) {
                return false;
            }
        }

     
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (grid[r][c] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean solveSudoku(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (solveSudoku(grid)) {
                                return true;
                            }
                            grid[row][col] = 0; 
                        }
                    }
                    return false; 
                }
            }
        }
        return true;
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(200, 100);
        Label label = new Label(message);
        Button button = new Button("OK");
        button.addActionListener(e -> dialog.dispose());
        dialog.add(label);
        dialog.add(button);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
     
        EventQueue.invokeLater(() -> new SudokuPuzzle());
    }
}
