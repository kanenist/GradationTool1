import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
public class Main extends JFrame {
    private JTextArea textArea;
    private JButton addButton;
    private JButton copyButton;
	private JButton clearButton;
    private ArrayList<String> textList;
    public Main() {
        textList = new ArrayList<>();

        setTitle("TJAgradationTool_1");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
        textArea = new JTextArea();
    	String def = "#BPM:120\n(グラデーションかける始点の曲自体のBPM)\n#startBPM:120\n(グラデーションかける始点の見た目BPM)\n#endBPM:240\n(グラデーションかける終点の見た目BPM)\n#howto:1\n（等差なら1,等比なら2）\n\n#START\n1,\n1,\n#END";
    	textArea.setText(def);
        addButton = new JButton("output.tjaの生成");
        copyButton = new JButton("生成結果のコピー");
    	clearButton = new JButton("デフォルトページを生成");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] lines = textArea.getText().split("\n");
                addLinesWithNumbers(lines);
            }
        });

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyTextToClipboard();
            	textList = new ArrayList<>();
            }
        });
    	
    	clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(def);
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(copyButton);
    	buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addLinesWithNumbers(String[] lines) {
    	String rs[] = Utils.textToTJA(lines);
    	if(textList.size() == 0){
    		for (int i = 0; i < rs.length; i++) {
            	textList.add(rs[i]);
    		}
        }
    }

    private void copyTextToClipboard() {
        StringBuilder textToCopy = new StringBuilder();
        for (String line : textList) {
            textToCopy.append(line).append("\n");
        }

        StringSelection selection = new StringSelection(textToCopy.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        JOptionPane.showMessageDialog(this, "output.tjaがコピーされました\nCtrl + Vなどでメモ帳などに貼り付けてください");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main gui = new Main();
                gui.setVisible(true);
            }
        });
    }
}