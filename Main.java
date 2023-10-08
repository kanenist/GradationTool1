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
    	String def = "#BPM:120\n(�O���f�[�V����������n�_�̋Ȏ��̂�BPM)\n#startBPM:120\n(�O���f�[�V����������n�_�̌�����BPM)\n#endBPM:240\n(�O���f�[�V����������I�_�̌�����BPM)\n#howto:1\n�i�����Ȃ�1,����Ȃ�2�j\n\n#START\n1,\n1,\n#END";
    	textArea.setText(def);
        addButton = new JButton("output.tja�̐���");
        copyButton = new JButton("�������ʂ̃R�s�[");
    	clearButton = new JButton("�f�t�H���g�y�[�W�𐶐�");
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
        JOptionPane.showMessageDialog(this, "output.tja���R�s�[����܂���\nCtrl + V�ȂǂŃ������Ȃǂɓ\��t���Ă�������");
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