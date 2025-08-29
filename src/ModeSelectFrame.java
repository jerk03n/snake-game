import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModeSelectFrame extends JFrame{
    private JButton withEnemyButton;
    private JButton withoutEnemyButton;
    private GameFrame gameFrame;

    public ModeSelectFrame() {
        setTitle("Select Game Mode");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.black);

        withoutEnemyButton = new JButton("Easy");
        withEnemyButton = new JButton("Hard");

        withoutEnemyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame = new GameFrame(false); 
                dispose();
            }
        });

        withEnemyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame = new GameFrame(true); 
                dispose(); 
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); 
        add(withoutEnemyButton, gbc);

        gbc.gridy = 1;
        add(withEnemyButton, gbc);


        setVisible(true);
    }
}
