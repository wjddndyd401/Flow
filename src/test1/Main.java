package test1;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.*;

class Frame extends JFrame {
	private String[] fixedExtensions = { "bat", "cmd", "com", "cpl", "exe", "scr", "js" };
	private GridBagLayout mainLayout;

	private JCheckBox[] checkBoxList;
	private JTextField newExtensionInput;
	private DefaultListModel<String> model;
	private JList<String> extensionList;

	private int maxExtensionLength = 20;
	private int maxExtensionCount = 200;

	public Frame() {
		setTitle("test");
		mainLayout = new GridBagLayout();
		this.setLayout(mainLayout);

		checkBoxList = new JCheckBox[fixedExtensions.length];
		newExtensionInput = new JTextField("", maxExtensionLength);
		model = new DefaultListModel<String>();
		extensionList = new JList<String>(model);

		// 좌측 라벨
		insert(new JLabel("고정 확장자"), 0, 0, 1, 1);
		insert(new JLabel("커스텀 확장자"), 0, 1, 1, 1);
		//고정 확장자 체크박스
		insert(getCheckBoxPanel(), 1, 0, 1, 1);
		// 커스텀 확장자 입력 폼
		insert(getCustomExtentionPanel(), 1, 1, 1, 1);
		
		initExtensionList();

		// 창 크기 맞춤
		this.pack();
		this.setVisible(true);
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
	
	public JPanel getCheckBoxPanel() {
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		for (int i = 0; i < fixedExtensions.length; i++) {
			checkBoxList[i] = new JCheckBox(fixedExtensions[i], false);
			checkBoxList[i].addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					write();
				}
			});
			checkBoxPanel.add(checkBoxList[i]);
		}
		
		return checkBoxPanel;
	}

	public JPanel getCustomExtentionPanel() {
		// 입력 폼 세로 정렬 및 패딩을 위한 내부 패널
		JPanel customExtensionPanel = new JPanel();
		customExtensionPanel.setLayout(new BoxLayout(customExtensionPanel, BoxLayout.Y_AXIS));
		customExtensionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// 버튼 가로 정렬을 위한 내부 패널
		JPanel newExtensionPanel = new JPanel();
		newExtensionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		// 입력 필드
		newExtensionInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				// 입력 최대 길이 제한
				JTextField textField = (JTextField) e.getSource();
				if (textField.getText().length() >= maxExtensionLength) {
					e.consume();
				}
			}
		});
		newExtensionPanel.add(newExtensionInput);

		// 추가삭제버튼
		JButton addButton = new JButton("추가");
		JButton deleteButton = new JButton("삭제");
		newExtensionPanel.add(addButton);
		newExtensionPanel.add(deleteButton);
		customExtensionPanel.add(newExtensionPanel);

		// 리스트
		JScrollPane extensionListPanel = new JScrollPane(extensionList);
		extensionListPanel.setBounds(0, 0, 300, 200);
		customExtensionPanel.add(extensionListPanel);

		// 추가버튼 이벤트
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newExtension = newExtensionInput.getText();
				if (newExtension.length() > 0) {
					addNewCustomExtension(newExtension);					
					write();
				}
			}
		});

		// 삭제버튼 이벤트
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = extensionList.getSelectedIndex();
				if (index >= 0) {
					model.remove(index);
					write();
				}
			}
		});

		return customExtensionPanel;
	}

	public void initExtensionList() {
		try {
			File file = new File("Output.txt");
			if (!file.exists()) {
				return;
			}

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				if (Arrays.asList(fixedExtensions).contains(line)) {
					int index = Arrays.asList(fixedExtensions).indexOf(line);
					checkBoxList[index].setSelected(true);
				} else {
					addNewCustomExtension(line);
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void addNewCustomExtension(String newExtension) {
		// 등록 최대 개수 제한
		if (model.getSize() >= maxExtensionCount) {
			return;
		}

		// 고정 확장자는 추가 불가능
		if (Arrays.asList(fixedExtensions).contains(newExtension)) {
			return;
		}

		// 중복 추가 불가능
		if (model.contains(newExtension)) {
			return;
		}

		// 검색 편의를 위해 정렬해서 표시
		ArrayList<String> list = Collections.list(model.elements());
		list.add(newExtension);
		Collections.sort(list);

		model.removeAllElements();
		for (String element : list) {
			model.addElement(element);
		}

		newExtensionInput.setText("");
		extensionList.ensureIndexIsVisible(model.getSize() - 1);
	}
	
	public void write() {
		try {
			File file = new File("Output.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fw);

			ArrayList<String> extensionList = Collections.list(model.elements());
			for (JCheckBox checkBox : checkBoxList) {
				if(checkBox.isSelected()) {
					extensionList.add(checkBox.getText());
				}
			}
			
			writer.write(String.join("\n", extensionList));
			writer.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}

public class Main {
	public static void main(String[] args) {
		new Frame();
	}
}
