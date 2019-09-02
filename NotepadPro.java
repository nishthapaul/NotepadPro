import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import java.lang.Runtime;
import javax.swing.JViewport;
import java.util.Scanner;
import javax.swing.filechooser.*;
import javafx.util.Pair; 
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import java.io.File;
public class NotepadPro implements ActionListener, ChangeListener{
	
	ArrayList<Pair<String,String>> list = new ArrayList<>();
	JFrame f;
	JPanel p, p1, newPanel;
	JTextArea textArea, textArea1, newTextArea;
	JScrollPane sp, sp1, newSP;
	JMenuBar menuBar;
	JMenu mFile, mEdit, mView, mRun;
	JMenuItem newFile, open, save, saveAs, exit, cut, copy, paste, undo, selectAll, redo, run, compile, format;
	JTabbedPane tabbedPane;
	
	int selectedIndex;
	JTextArea selectedTextArea;
	JScrollPane selectedScrollPane;
	JPanel selectedPanel;
	
	UndoManager manager;
	Scanner sc;
	JFileChooser fileChooser;
	String fileCompileBat = "Compile.bat";
	String fileRunBat = "Run.bat";
	
	public void setMenuBar(){
		menuBar = new JMenuBar();
		mFile = new JMenu("File");
		newFile = new JMenuItem("New");
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		saveAs = new JMenuItem("SaveAs");
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		mFile.add(newFile); mFile.add(open); mFile.add(save); mFile.add(saveAs); mFile.add(exit);
		
		mEdit = new JMenu("Edit");
		undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.ALT_MASK));
		redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.ALT_MASK));
		cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
		copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
		selectAll = new JMenuItem("Select All");
		selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		mEdit.add(undo); mEdit.add(redo); mEdit.addSeparator(); mEdit.add(cut); mEdit.add(copy); mEdit.add(paste); mEdit.add(selectAll);
		
		mView = new JMenu("View");
		format = new JMenuItem("Format");
		format.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		mView.add(format);
		
		mRun = new JMenu("Run");
		compile = new JMenuItem("Compile");
		compile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		run = new JMenuItem("Run");
		run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		mRun.add(compile); mRun.add(run);
		
		menuBar.add(mFile);
		menuBar.add(mEdit);
		menuBar.add(mView);
		menuBar.add(mRun);
		
		f.setJMenuBar(menuBar);
	}
	NotepadPro(){
		f = new JFrame("Notepad Pro");
		// p = new JPanel();
		// f.add(p);
		
		setMenuBar();
		manager = new UndoManager();
		fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		newFile.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		exit.addActionListener(this);
		undo.addActionListener(this);
		redo.addActionListener(this);
		cut.addActionListener(this);
		copy.addActionListener(this);
		paste.addActionListener(this);
		selectAll.addActionListener(this);
		format.addActionListener(this);
		compile.addActionListener(this);
		run.addActionListener(this);
		
		tabbedPane = new JTabbedPane();
		p = new JPanel();
		p.setLayout(new BorderLayout());
		textArea = new JTextArea(1100, 630);
		sp = new JScrollPane(textArea);
		p.add(sp);
		tabbedPane.addTab("Untitled", p);
		list.add(new Pair <String,String> (null, null));
		tabbedPane.setSelectedIndex(0);
		
		selectedIndex = 0;
		selectedPanel = p;
		selectedScrollPane = sp;
		selectedTextArea = textArea;
		
		selectedTextArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				manager.addEdit(e.getEdit());
			}
		});
		
		tabbedPane.addChangeListener(this);
		f.add(tabbedPane);
		
		f.setSize(1200, 650);
		f.setLocation(50, 50);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void stateChanged(ChangeEvent e) {
		
        selectedIndex = tabbedPane.getSelectedIndex();
        System.out.println(selectedIndex + "");
		
		selectedPanel = (JPanel)tabbedPane.getSelectedComponent();
		System.out.println(" ");
		Component[] componentsSP = selectedPanel.getComponents();
		for (Component component : componentsSP) {
			if (component.getClass().equals(JScrollPane.class)) {
				selectedScrollPane = (JScrollPane)component;
				selectedTextArea = (JTextArea) (((JViewport) (selectedScrollPane.getViewport())).getView());
				System.out.println("text -> " + selectedTextArea.getText());
				break;
			}
		}
		
		selectedTextArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				manager.addEdit(e.getEdit());
			}
		});
    }
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == newFile){
			
			newPanel = new JPanel();
			newPanel.setLayout(new BorderLayout());
			newTextArea = new JTextArea(1100, 630);
			newSP = new JScrollPane(newTextArea);
			newPanel.add(newSP);
			String s = "Untitled" + tabbedPane.getTabCount();
			tabbedPane.addTab(s, newPanel);
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
			list.add(new Pair <String,String> (null, null));
			
		} else if(ae.getSource() == open){
			
			newPanel = new JPanel();
			newPanel.setLayout(new BorderLayout());
			newTextArea = new JTextArea(1100, 630);
			
			int n = fileChooser.showOpenDialog(null);
			if(n == JFileChooser.APPROVE_OPTION){
				try{
					sc = new Scanner(fileChooser.getSelectedFile());
					while(sc.hasNext()){
						String str = sc.nextLine();
						newTextArea.setText(newTextArea.getText() + str + "\n");
					}
				} catch (FileNotFoundException e){
					System.out.println(e);
				}
			}
			
			newSP = new JScrollPane(newTextArea);
			newPanel.add(newSP);
			String s = fileChooser.getSelectedFile().getName();
			tabbedPane.addTab(s, newPanel);
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
			list.add(new Pair <String,String> (s, fileChooser.getSelectedFile().getAbsolutePath()));
			
		} else if(ae.getSource() == save){
			System.out.println(list.get(selectedIndex).getKey());
			PrintWriter pr = null;
			
			if(selectedTextArea.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "empty document cannot be saved");
				return;
			}
			
			if(list.get(selectedIndex).getKey() == null){
				System.out.println("entered");
				// save as -> save in list
				int n = fileChooser.showSaveDialog(null);
				if(n == JFileChooser.APPROVE_OPTION){
					try{
						String fileName = fileChooser.getSelectedFile().getName();
						String fileDirectoryPath = fileChooser.getSelectedFile().getAbsolutePath();
						
						pr = new PrintWriter(new FileWriter(fileDirectoryPath));
						String textToBeSaved = selectedTextArea.getText();
						StringTokenizer st = new StringTokenizer(textToBeSaved, "\n");
						while(st.hasMoreElements()){
							pr.println(st.nextToken());
						}
						list.set(selectedIndex, new Pair <String,String> (fileName, fileDirectoryPath));
						tabbedPane.setTitleAt(selectedIndex, fileName);
						for (Pair <String,String> temp : list)
							System.out.println(temp);
					} catch (Exception e){
						System.out.println(e);
					} finally{
						pr.close();
					}
				}
			} else {
				// save -> print again in same directory
				try{
						String fileName = list.get(selectedIndex).getKey();
						String fileDirectoryPath = list.get(selectedIndex).getValue();
						
						pr = new PrintWriter(new FileWriter(fileDirectoryPath));
						System.out.println("dr path " + fileDirectoryPath);
						String textToBeSaved = selectedTextArea.getText();
						StringTokenizer st = new StringTokenizer(textToBeSaved, "\n");
						while(st.hasMoreElements()){
							pr.println(st.nextToken());
						}
						for (Pair <String,String> temp : list)
							System.out.println(temp);
					} catch (Exception e){
						System.out.println(e);
					} finally{
						pr.close();
					}
				
			}
			
		} else if(ae.getSource() == saveAs){
			
			if(selectedTextArea.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "empty document cannot be saved");
				return;
			}
			PrintWriter pr = null;
				int n = fileChooser.showSaveDialog(null);
				if(n == JFileChooser.APPROVE_OPTION){
					try{
						String fileName = fileChooser.getSelectedFile().getName();
						String fileDirectoryPath = fileChooser.getSelectedFile().getAbsolutePath();
						
						pr = new PrintWriter(new FileWriter(fileDirectoryPath));
						String textToBeSaved = selectedTextArea.getText();
						StringTokenizer st = new StringTokenizer(textToBeSaved, "\n");
						while(st.hasMoreElements()){
							pr.println(st.nextToken());
						}
						list.set(selectedIndex, new Pair <String,String> (fileName, fileDirectoryPath));
						for (Pair <String,String> temp : list)
							System.out.println(temp);
					} catch (Exception e){
						System.out.println(e);
					} finally{
						pr.close();
					}
				}
			
		} else if(ae.getSource() == exit){
			System.exit(0);
		} else if(ae.getSource() == undo){
			manager.undo();
		} else if(ae.getSource() == redo){
			manager.redo();
		} else if(ae.getSource() == cut){
			selectedTextArea.cut();
		} else if(ae.getSource() == copy){
			selectedTextArea.copy();
		} else if(ae.getSource() == paste){
			selectedTextArea.paste();
		} else if(ae.getSource() == selectAll){
			selectedTextArea.selectAll();
			// selectAll.transferFocusBackward(); is line ka koi fayda nai hua
		} else if(ae.getSource() == format){
			
			JFontChooser fd = new JFontChooser(f,selectedTextArea.getFont());
			fd.show();
			if(fd.getReturnStatus() == fd.RET_OK){
				   selectedTextArea.setFont(fd.getFont());
			}
			fd.dispose();
			
			
		} else if(ae.getSource() == compile){
			
			String fileName = list.get(selectedIndex).getKey();
			String fileDirectoryPath = list.get(selectedIndex).getValue();
			System.out.println(fileDirectoryPath);
			if(fileName.endsWith(".java")){
				String fileDirectoryOnly = fileDirectoryPath.substring(0, fileDirectoryPath.lastIndexOf('\\') + 1);
				fileCompileBat = fileDirectoryOnly + fileCompileBat;
				PrintWriter pw = null;
				try{
					pw = new PrintWriter(fileCompileBat);
					pw.println("javac " + fileDirectoryPath);
					System.out.println("fileCompilePath " + fileCompileBat);
					
				} catch(Exception e){
					System.out.println(e);
				} finally{
					pw.close();
				}
				
				try{
					Process p = Runtime.getRuntime().exec(fileCompileBat);
				}
				catch(Exception e){
					System.out.println(e);
				}
			} else{
				JOptionPane.showMessageDialog(null, "It is not a java file");
			}
			
		} else if(ae.getSource() == run){
			String fileName = list.get(selectedIndex).getKey();
			String fileDirectoryPath = list.get(selectedIndex).getValue();
			System.out.println(fileDirectoryPath);
			if(fileName.endsWith(".java")){
				String fileDirectoryOnly = fileDirectoryPath.substring(0, fileDirectoryPath.lastIndexOf('\\') + 1);
				fileRunBat = fileDirectoryOnly + fileRunBat;
				String fileNameOnly = fileDirectoryPath.substring(0, fileDirectoryPath.lastIndexOf('.'));
				System.out.println(fileDirectoryOnly);
				String fileClassPath = fileNameOnly + ".class";
				File file = new File(fileClassPath);
				if(!file.exists()){
					JOptionPane.showMessageDialog(null, "Compile it first");
				} else {
					PrintWriter pw = null;
					try{
						pw = new PrintWriter(fileRunBat);
						pw.println("java " + fileName.substring(0, fileName.lastIndexOf('.')) + " > " + fileDirectoryOnly +"output.txt");
						
					} catch(Exception e){
						System.out.println(e);
					} finally{
						pw.close();
					}
					
					try{
						Process p = Runtime.getRuntime().exec(fileRunBat);
					}
					catch(Exception e){
						System.out.println(e);
					}
				}
			} else{
				JOptionPane.showMessageDialog(null, "It is not a java file");
			}
			
		}
	}
	public static void main(String str[]){
		new NotepadPro();
	}
}