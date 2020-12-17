package gameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JFrame implements ActionListener{
    private JTextField textGetId;
    private JComboBox<Integer> comboBox;
    private JButton buttonStart;

    String id;
    int level;

    public StartPanel(){
        super("Ex2 - Pokemon Game");
        initFrame();
        initLabel();
        setLayout(null);
        setVisible(true);
    }

    private void initFrame(){
        setSize(300,200);//Size of frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Stop the Program
        ImageIcon iconApplication = new ImageIcon("./resources/iconApplication.png");//create icon application
        setIconImage(iconApplication.getImage());//add icon application
        setLocationRelativeTo(null);//open the window in center
    }

    private void initLabel(){
        JLabel welcomeText = new JLabel();
        welcomeText.setBounds(10,0,150,20);
        welcomeText.setText("To Start The Game");

        JLabel idText = new JLabel();
        idText.setBounds(10,40,250,20);
        idText.setText("insert id:");

        textGetId = new JTextField();//insert text
        textGetId.setBounds(100,40,150,20);

        JLabel levelText = new JLabel();
        levelText.setBounds(10,60,250,20);
        levelText.setText("Select Level:");

        Integer[] levels = new Integer[24];
        for(int i = 0; i < 24; i++){
            levels[i] = i;
        }
        comboBox = new JComboBox<>(levels);//box of levels
        comboBox.setBounds(100,60,50,20);
        comboBox.setEditable(true);
        comboBox.addActionListener(this);

        buttonStart = new JButton("start game");
        buttonStart.setBounds(150,100,100,30);
        buttonStart.addActionListener(this);

        add(welcomeText);
        add(idText);
        add(textGetId);
        add(levelText);
        add(buttonStart);
        add(comboBox);
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttonStart){
            level = comboBox.getSelectedIndex();
            id = textGetId.getText();
            System.out.println("ID:" +id +"," +"Level:" +level);
        }
    }
}
