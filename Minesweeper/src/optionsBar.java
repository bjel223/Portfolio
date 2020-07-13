/*optionsBar establishes the bar which is added to the top of the frame with game settings, a timer, and options for
*restarting, quiting, and needing help
*Author:Benjamin Ellis
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class optionsBar {
    private static int BEGINNERSIZE = 4;//bigginersize,biginnerbombs,minsize,maxsize,and minbombs established as static ints
    private static int BEGINNERBOMBS = 4;//this is in case the defaults needed to be changed later on
    private static int MINSIZE = 3;
    private static int MAXSIZE = 12;
    private static int MINBOMBS = 2;
    private JMenuBar options;
    private JMenuItem restart;
    private JMenuItem timer;
    private JMenu settings;
    private JMenuItem help;
    private JMenuItem quit;
    private ButtonGroup difficulty;
    private JRadioButtonMenuItem beginner;
    private JRadioButtonMenuItem intermediate;
    private JRadioButtonMenuItem expert;
    private JRadioButtonMenuItem custom;
    private int cSize = BEGINNERSIZE,cBombs=BEGINNERBOMBS;

    public optionsBar(ActionListener DL, ActionListener OL){
        options = new JMenuBar();//menu bar, menu items, and buttons are established in the following lines
        timer = new JMenuItem("Time: 0");
        restart = new JMenuItem("Restart");
        settings = new JMenu("Settings");
        help = new JMenuItem("Help");
        quit = new JMenuItem("Quit");
        beginner = new JRadioButtonMenuItem("Beginner");
        intermediate = new JRadioButtonMenuItem("Intermediate");
        expert = new JRadioButtonMenuItem("Expert");
        custom = new JRadioButtonMenuItem("Custom");
        difficulty = new ButtonGroup();
        beginner.setMnemonic(KeyEvent.VK_B);//Allows settings options to be selected with a keyboard in addition to the mouse
        intermediate.setMnemonic(KeyEvent.VK_I);
        expert.setMnemonic(KeyEvent.VK_E);
        custom.setMnemonic(KeyEvent.VK_C);
        beginner.setActionCommand("Beginner");//action commands used to determine the selected difficulty
        intermediate.setActionCommand("Intermediate");
        expert.setActionCommand("Expert");
        custom.setActionCommand("Custom");
        difficulty.add(beginner);//difficulties grouped so only one may be selected
        difficulty.add(intermediate);
        difficulty.add(expert);
        difficulty.add(custom);
        settings.add(beginner);//difficulties added to the settings menu
        settings.add(intermediate);
        settings.add(expert);
        settings.add(custom);
        options.add(settings);//all menus and menu items added to the menu bar
        options.add(timer);
        options.add(restart);
        options.add(help);
        options.add(quit);
        beginner.addActionListener(DL);//Action listeners given to each menu item and buttom
        intermediate.addActionListener(DL);
        expert.addActionListener(DL);
        custom.addActionListener(DL);
        restart.addActionListener(OL);
        restart.setActionCommand("Restart");
        help.addActionListener(OL);
        help.setActionCommand("Help");
        quit.addActionListener(OL);
        quit.setActionCommand("Quit");
    }
    public JMenuBar getOptionsMenu(){
        return options;
    }
    public void showHelp(){//Help message that is displayed when help is clicked
        JOptionPane.showMessageDialog(help,"Welcome to Minesweeper!" +
                "\nThe goal of the game is to clear all non-bomb tiles." +
                "\nLeft-click on tiles to reveal what they are and clear tiles around them until they neighbor bombs." +
                "\nNumbers on revealed tiles tell you how many bombs they neighbor." +
                "\nAvoid hitting a bomb, or you'll lose." +
                "\nChange the board size and number of bombs in the settings menu based on your skill level, or create a custom setup." +
                "\nGood Luck!");
    }
    public void customDifficulty(){//custom difficulty prompt for when this difficulty is selected in the settings
        JPanel customOptions = new JPanel();
        JTextField customSize = new JTextField(2);
        JTextField customBombs = new JTextField(3);
        JLabel sizeLabel = new JLabel("Size(#rows/columns): ");
        JLabel bombLabel = new JLabel("Number of Bombs: ");
        customOptions.setLayout(new GridLayout(2,2,0,0));
        customOptions.add(sizeLabel);
        customOptions.add(customSize);
        customOptions.add(bombLabel);
        customOptions.add(customBombs);
        int confirmation = JOptionPane.showConfirmDialog(null,customOptions,"Enter your custom options here.",JOptionPane.OK_CANCEL_OPTION);
        if(confirmation == JOptionPane.OK_OPTION){
            if(!customSize.getText().equalsIgnoreCase("")&&!customBombs.getText().equalsIgnoreCase("")){//Preventative statement in case either field is empty when values are submitted

                /*
                 *Due to the nature of how input is received for the custom difficulty values, only integer values will work correctly
                 * The string received is converted to an integer, meaning an error emerges when characters are input instead
                 */

                cSize = Integer.parseInt(customSize.getText());//input converted to integer values
                cBombs = Integer.parseInt(customBombs.getText());
                if (cSize > MAXSIZE || cSize < MINSIZE || cBombs < MINBOMBS || cBombs > cSize * cSize / 2) {
                    cSize = BEGINNERSIZE;
                    cBombs = BEGINNERBOMBS;
                    JOptionPane.showMessageDialog(null, "One or more given constraints invalid." +//error message for invalid input
                            "\nSize must be between 3 and 12. The Number of bombs must be between 2 and half of the total spaces." +
                            "\nThe size and bomb number have defaulted to beginner values.");

                }
            }
            else{
                cSize = BEGINNERSIZE;
                cBombs = BEGINNERBOMBS;
                JOptionPane.showMessageDialog(null, "One or more given constraints invalid." +
                        "\nSize must be between 3 and 12. The Number of bombs must be between 2 and half of the total spaces." +
                        "\nThe size and bomb number have defaulted to beginner values.");
            }
        }


    }
    public int getcSize(){//returns custom size
        return cSize;
    }
    public int getcBombs(){//returns custom bomb count
        return cBombs;
    }
    public void incrementTime(int time){//time incremented for the timer in the options baropti
        timer.setText("Time: "+time);

    }

}
