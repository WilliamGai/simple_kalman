package baoutil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class LogBatJFrame extends JFrame implements ActionListener {
	private JPanel panel;
	private JTextPane textPane;

	public LogBatJFrame(String name){
		super("(" + name + ")t4game");
		this.setBounds(200, 100, 1020, 604);
		this.setDefaultCloseOperation(3);

		panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.LIGHT_GRAY);

		// font = new Font("Consolas", 0, 25);

		textPane = new JTextPane();

		// textPane.setFont(font);
		textPane.setBackground(Color.DARK_GRAY);

		Container ct = this.getContentPane();
		ct.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));

		panel.add(new JScrollPane(textPane));
		panel.setPreferredSize(new Dimension(1016, 600));
		ct.add(panel);

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

	}

	public void setDocs(String str, Color col, Color b_col, Boolean bold,
			Integer fontSize) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();

		if (col == null)
			col = Color.green;
		StyleConstants.setForeground(attrSet, col);
		// 颜色
		if (bold == null)
			bold = false;
		StyleConstants.setBold(attrSet, bold);

		if (b_col != null)
			StyleConstants.setBackground(attrSet, b_col);
		// StyleConstants.setItalic(attrSet, true);

		// 字体大小
		if (fontSize == null)
			fontSize = 15;
		StyleConstants.setFontSize(attrSet, fontSize);
		StyleConstants.setFontFamily(attrSet, "Consolas");
		insert(str, attrSet);
	}

	public void insert(String str, AttributeSet attrSet) {
		Document doc = textPane.getDocument();
		str = "\n" + str;
		try {
//			doc.insertString(doc.getLength(), str, attrSet);
			doc.insertString(doc.getLength(), str, attrSet);
		} catch (BadLocationException e) {
			System.out.println("BadLocationException:   " + e);
		}
	}
}