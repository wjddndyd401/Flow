package test1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Frame extends JFrame {
	private String[] fixedExtensions = {"bat", "cmd", "com", "cpl", "exe", "scr", "js"};
	GridBagLayout mainLayout;
	
	public Frame() {		
		setTitle("test");
		mainLayout = new GridBagLayout();
		this.setLayout(mainLayout);
		
		//좌측 라벨
		insert(new JLabel("고정 확장자"), 0, 0, 1, 1);
		insert(new JLabel("커스텀 확장자"), 0, 1, 1, 1);

		//고정 확장자 체크박스
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		for(int i = 0; i < fixedExtensions.length; i++) {
			JCheckBox checkBox = new JCheckBox(fixedExtensions[i], false);
			checkBoxPanel.add(checkBox);
		}
		insert(checkBoxPanel, 1, 0, 1, 1);
		
		//커스텀 확장자 입력 폼
		insert(getCustomExtentionPanel(), 1, 1, 1, 1);
		
		//창 크기 맞춤
		this.pack();
		this.setVisible(true);
	}
	
	public JPanel getCustomExtentionPanel() {
		//입력 폼 세로 정렬 및 패딩을 위한 내부 패널
		JPanel customExtensionPanel = new JPanel();		
		customExtensionPanel.setLayout(new BoxLayout(customExtensionPanel, BoxLayout.Y_AXIS));
		customExtensionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//버튼 가로 정렬을 위한 내부 패널
		JPanel newExtensionPanel = new JPanel();
		newExtensionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//입력 필드
		JTextField newExtensionInput = new JTextField("", 20);
		newExtensionInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				//입력 최대 길이 20자 제한
				if(((JTextField) e.getSource()).getText().length() >= 20) {
					e.consume();
				}
			}
		});
		newExtensionPanel.add(newExtensionInput);
		
		//추가삭제버튼
		JButton addButton = new JButton("추가");
		JButton deleteButton = new JButton("삭제");
		newExtensionPanel.add(addButton);
		newExtensionPanel.add(deleteButton);
		customExtensionPanel.add(newExtensionPanel);
		
		//리스트
		DefaultListModel<String> model = new DefaultListModel<String>();
		JList<String> extensionList = new JList<String>(model);
		JScrollPane extensionListPanel = new JScrollPane(extensionList);
		extensionListPanel.setBounds(0, 0, 300, 200);
		customExtensionPanel.add(extensionListPanel);

		//추가버튼 이벤트
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newExtension = newExtensionInput.getText();
				if(newExtension.length() > 0) {
					model.addElement(newExtension);
					newExtensionInput.setText("");
					extensionList.ensureIndexIsVisible(model.getSize() - 1);
				}
			}
		});
		
		//삭제버튼 이벤트
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = extensionList.getSelectedIndex();
				if(index >= 0) {
					model.remove(index);
				}
			}
		});
		
		return customExtensionPanel;
	}
	
	public void insert(Component component, int x, int y, int width, int height) {
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridx = x;
		constraint.gridy = y;
		constraint.gridwidth = width;
		constraint.gridheight = height;
		mainLayout.setConstraints(component, constraint);
		this.add(component);
	}
}

public class main {
	public static void main(String[] args) {
		new Frame();
	}
}
