import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class GraphicQuest extends JFrame {
    private final int SIZE = 5;
    private int hp = 3;
    private int difficulty;
    private int heroR, heroC, castleR, castleC;

    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private JLabel statusLabel = new JLabel();
    private String[][] monsters = new String[SIZE][SIZE];

    public GraphicQuest() {
        setupGame();
        initUI();
    }

    private void setupGame() {
        // Выбор сложности
        String[] options = {"Легко", "Средне", "Сложно"};
        int choice = JOptionPane.showOptionDialog(null, "Выберите сложность", "Начало игры",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        difficulty = choice + 1;

        Random rand = new Random();
        castleR = 0; castleC = rand.nextInt(SIZE);
        heroR = SIZE - 1; heroC = rand.nextInt(SIZE);

        // Генерация монстров
        int mCount = difficulty * 2;
        while (mCount > 0) {
            int r = rand.nextInt(SIZE), c = rand.nextInt(SIZE);
            if ((r != castleR || c != castleC) && (r != heroR || c != heroC) && monsters[r][c] == null) {
                monsters[r][c] = (mCount % 2 == 0) ? "Small" : "Big";
                mCount--;
            }
        }
    }

    private void initUI() {
        setTitle("Adventure Quest");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(SIZE, SIZE));
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                buttons[r][c] = new JButton("⬜");
                buttons[r][c].setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
                int finalR = r, finalC = c;
                buttons[r][c].addActionListener(e -> handleMove(finalR, finalC));
                grid.add(buttons[r][c]);
            }
        }

        updateBoard();
        add(statusLabel, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        setSize(500, 550);
        setVisible(true);
    }

    private void updateBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (r == heroR && c == heroC) buttons[r][c].setText("🧙");
                else if (r == castleR && c == castleC) buttons[r][c].setText("🏰");
                else buttons[r][c].setText("⬜");
            }
        }
        statusLabel.setText(" Жизни: " + "❤️".repeat(hp) + "🖤".repeat(3 - hp));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
    }

    private void handleMove(int r, int c) {
        // Проверка дистанции хода (соседние клетки)
        if (Math.abs(r - heroR) <= 1 && Math.abs(c - heroC) <= 1) {
            heroR = r; heroC = c;
            checkEvent();
            updateBoard();
        } else {
            JOptionPane.showMessageDialog(this, "Слишком далеко! Ходите на соседние клетки.");
        }
    }

    private void checkEvent() {
        if (heroR == castleR && heroC == castleC) {
            JOptionPane.showMessageDialog(this, "ПОБЕДА! Вы в замке!");
            System.exit(0);
        }

        if (monsters[heroR][heroC] != null) {
            String type = monsters[heroR][heroC];
            if (askQuestion(type)) {
                JOptionPane.showMessageDialog(this, "Монстр побежден!");
                monsters[heroR][heroC] = null;
            } else {
                hp--;
                JOptionPane.showMessageDialog(this, "Ошибка! Потеряна жизнь.");
                if (hp <= 0) {
                    updateBoard();
                    JOptionPane.showMessageDialog(this, "ИГРА ОКОНЧЕНА 💀");
                    System.exit(0);
                }
            }
        }
    }

    private boolean askQuestion(String type) {
        Random rand = new Random();
        int a, b,


correct;
        if (type.equals("Small")) {
            a = rand.nextInt(10); b = rand.nextInt(10);
            correct = a + b;
            String ans = JOptionPane.showInputDialog("Маленький монстр! Реши: " + a + " + " + b);
            return ans != null && ans.equals(String.valueOf(correct));
        } else {
            a = rand.nextInt(10 * difficulty); b = rand.nextInt(10);
            correct = a * b;
            String ans = JOptionPane.showInputDialog("БОЛЬШОЙ монстр! Реши: " + a + " * " + b);
            return ans != null && ans.equals(String.valueOf(correct));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphicQuest::new);
    }
}