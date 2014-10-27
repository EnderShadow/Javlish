package matt.util.javlish.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import matt.util.javlish.Processor;

public class GUI implements Runnable
{
	public static final JTextArea display = new JTextArea(3, 50);
	public static final JTextArea input = new JTextArea(19, 50);
	public JFileChooser fc;
	public static JButton destination;
	public static JButton process;
	
	public GUI()
	{
		fc = new JFileChooserNew();
		fc.setCurrentDirectory(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()));
		Thread runner = new Thread(this);
		runner.start();
	}

	public void run()
	{
		setFrame();
	}
	
	public void setFrame()
	{
		//main screen
		final JFrame mainScreen = new JFrame("Easier Java");
		mainScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainScreen.setLayout(new FlowLayout());
		mainScreen.setSize(600, 500);
		mainScreen.setLocationRelativeTo(null);
		mainScreen.setVisible(true);
		
		fc.setFileFilter(new FileNameExtensionFilter("javlish files (*.javlish)", "javlish"));
		
		JPanel inputText = new JPanel();
		input.setEditable(true);
		input.setLineWrap(true);
		input.setWrapStyleWord(true);
		JScrollPane scroll2 = new JScrollPane(input);
		scroll2.setAutoscrolls(true);
		inputText.add(scroll2);
		
		JPanel displayText = new JPanel();
		display.setEditable(false);
		display.setForeground(Color.red);
		JScrollPane scroll = new JScrollPane(display);
		scroll.setAutoscrolls(true);
		displayText.add(scroll);
		
		destination = new JButton("Save As");
		destination.setFocusable(false);
		process = new JButton("Save");
		process.setFocusable(false);
		final JCheckBox javaFile = new JCheckBox(".java");
		final JCheckBox classFile = new JCheckBox(".class");
		javaFile.setFocusable(false);
		classFile.setFocusable(false);
		
		JPanel files = new JPanel();
		JLabel openFileDir = new JLabel("Save to: ", JLabel.LEFT);
		final JTextField openFile = new JTextField(45);
		files.add(openFileDir);
		files.add(openFile);
		
		destination.addActionListener(e -> {
			fc.showSaveDialog(mainScreen);
			openFile.setText(fc.getSelectedFile().getAbsolutePath());
		});
		
		process.addActionListener(a -> {
			File file = fc.getSelectedFile();
			if(file != null)
			{
				if(!file.getName().endsWith(".javlish"))
				{
					display.append("Invalid file extension\n");
				}
				else
				{
					if(!file.exists())
					{
						try
						{
							file.createNewFile();
						}
						catch(Exception e)
						{
							display.append("Failed to create file\n");
							e.printStackTrace();
						}
					}

					File file2 = new File(file.getPath().substring(0, file.getPath().length() - 4) + "a");

					try
					{
						if(file.exists())
						{
							FileWriter fw = new FileWriter(file, false);
							fw.write(input.getText());
							fw.close();

							String filename = file2.getName().substring(0, file2.getName().length() - 5);
							if(javaFile.isSelected())
							{
								fw = new FileWriter(file2, false);
								String newLine = System.getProperty("line.separator");
								String java = Processor.toJava(input.getText());
								String[] javaa = java.split("\n");
								for(String str : javaa)
								{
									fw.write(str);
									fw.write(newLine);
								}
								fw.close();
							}
							if(classFile.isSelected())
							{
								new File(filename + ".class").createNewFile();
								//TODO generate class file
							}
						}
					}
					catch(Exception e)
					{
						display.append("Failed to save file\n");
						e.printStackTrace();
					}
				}
			}
			else
			{
				fc.showSaveDialog(mainScreen);
				openFile.setText(fc.getSelectedFile().getAbsolutePath());
			}
		});

		mainScreen.add(inputText);
		mainScreen.add(files);
		mainScreen.add(destination);
		mainScreen.add(process);
		mainScreen.add(javaFile);
		mainScreen.add(classFile);
		mainScreen.add(displayText);
		mainScreen.validate();
	}
	
	private class JFileChooserNew extends JFileChooser
	{
		private static final long serialVersionUID = 3309604318905334927L;
		
		@Override
		public void approveSelection()
		{
			super.approveSelection();
			if(fc.getSelectedFile() != null && !fc.getSelectedFile().getName().contains("."))
			{
				fc.setSelectedFile(new File(fc.getSelectedFile().getPath() + ".javlish"));
			}
			process.getActionListeners()[0].actionPerformed(null);
		}
		
		@Override
		public void cancelSelection()
		{
			super.cancelSelection();
			this.setSelectedFile(null);
		}
	}
}