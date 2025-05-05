import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.util.logging.Logger;

public class JDice {
    static final String CLEAR = "Clear";
    static final String ROLL = "Roll Selection";

    static void showError(String s) {
        JOptionPane.showMessageDialog(null, s, "Error", "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Lớp xử lý sự kiện từ JComboBox và các nút.
     * 
     * Refactor:
     * - Đổi tên constructor sai từ `JDice-Listener` thành hợp lệ `JDiceListener`.
     * - Sửa cú pháp thiếu dấu `;` ở biến name.
     * 
     * Thêm mới:
     * - Logging bằng java.util.logging để ghi nhận hành động người dùng.
     */
    private static class JDiceListener implements ActionListener {
        Vector<String> listItems;
        JList resultList;
        JComboBox inputBox;
        long lastEvent;
        private static final Logger logger = Logger.getLogger(JDiceListener.class.getName());

        public JDiceListener(JList resultList, JComboBox inputBox) {
            this.listItems = new Vector<String>();
            this.resultList = resultList;
            this.inputBox = inputBox;
            this.lastEvent = 0;
        }

        public void actionPerformed(ActionEvent e) {
            logger.info("Action received: " + e.getActionCommand());

            if (e.getWhen() == lastEvent)
                return;
            lastEvent = e.getWhen();

            if (e.getSource() instanceof JComboBox || e.getActionCommand().equals(ROLL)) {
                String s = inputBox.getSelectedItem().toString();
                String[] arr = s.split("=");
                String name = "";
                for (int i = 0; i < arr.length - 2; i++) {
                    name = name + arr[i] + "=";
                }
                if (arr.length >= 2)
                    name = name + arr[arr.length - 2];
                doRoll(name, arr[arr.length - 1]);
            } else if (e.getActionCommand().equals(CLEAR)) {
                doClear();
            }
        }

        private void doClear() {
            resultList.clearSelection();
            listItems.clear();
            resultList.setListData(listItems);
        }

        private void doRoll(String name, String diceString) {
            String prepend = "";
            int start = 0;
            int i;
            Vector<DieRoll> v = DiceParser.parseRoll(diceString);
            if (v == null) {
                showError("Invalid dice string: " + diceString);
                return;
            }
            if (name != null) {
                listItems.add(0, name);
                start = 1;
                prepend = "  ";
            }
            int[] selectionIndices = new int[start + v.size()];
            for (i = 0; i < v.size(); i++) {
                DieRoll dr = v.get(i);
                RollResult rr = dr.makeRoll();
                String toAdd = prepend + dr + "  =>  " + rr;
                listItems.add(i + start, toAdd);
            }
            for (i = 0; i < selectionIndices.length; i++) {
                selectionIndices[i] = i;
            }
            resultList.setListData(listItems);
            resultList.setSelectedIndices(selectionIndices);
        }
    }

    public static void main(String[] args) {
        Vector<String> v = new Vector<String>();
        if (args.length >= 1) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(args[0]));
                String s;
                while ((s = br.readLine()) != null) {
                    v.add(s);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("***********\n**********\n");
                System.err.println("Could not read input file: " + args[0]);
                System.err.println("***********\n**********\n");
            }
        }

        JFrame jf = new JFrame("Dice Roller");
        Container c = jf.getContentPane();
        c.setLayout(new BorderLayout());
        JList jl = new JList();
        c.add(jl, BorderLayout.CENTER);
        JComboBox jcb = new JComboBox(v);
        jcb.setEditable(true);
        c.add(jcb, BorderLayout.NORTH);
        JDiceListener jdl = new JDiceListener(jl, jcb);
        jcb.addActionListener(jdl);
        JPanel rightSide = new JPanel();
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
        String[] buttons = { ROLL, "d4", "d6", "d8", "d10", "d12", "d20", "d100", CLEAR };
        for (int i = 0; i < buttons.length; i++) {
            JButton newButton = new JButton(buttons[i]);
            rightSide.add(newButton);
            newButton.addActionListener(jdl);
        }
        c.add(rightSide, BorderLayout.EAST);
        jf.setSize(450, 500);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
