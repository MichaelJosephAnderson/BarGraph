package barGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class OptionsPanel extends JPanel implements ActionListener {
  
  private JButton[] buttons;
  private LayoutPanel layoutP;
  private JComboBox<String> sortChoices;
  private JCheckBox autorunToggle; 
  
  public OptionsPanel(LayoutPanel lp) {
    layoutP = lp;
    
    buttons = new JButton[4];
    buttons[0] = new JButton("add");
    buttons[1] = new JButton("randomize");
    buttons[2] = new JButton("step");
    buttons[3] = new JButton("sort");
    
    for (JButton b : buttons) {
      this.add(b);
      b.addActionListener(this);
    }
    sortChoices = new JComboBox<String>();
    sortChoices.addItem("Bubble sort");
    sortChoices.addItem("Selection sort");
    sortChoices.addItem("Insertion sort");
    sortChoices.addItem("Merge sort");
    this.add(sortChoices);
    
    autorunToggle = new JCheckBox("Autorun");
    this.add(autorunToggle);
    
  }
  
  

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (arg0.getSource() instanceof JButton) {
      JButton b = (JButton) arg0.getSource();
      //System.out.println("button " + b.getText() + " was pressed");
      
      if (b.getText().equals("add")) {
        layoutP.performOption(OptionEvent.ADD);
      } else if (b.getText().equals("randomize")) {
        layoutP.performOption(OptionEvent.RANDOMIZE);
      } else if (b.getText().equals("sort")) {
        layoutP.performOption(OptionEvent.SORT);
      } else if (b.getText().equals("step")) {
        layoutP.performOption(OptionEvent.STEP);
      }
    }
  }
  
  public String getSortChoice() {
	  return (String) sortChoices.getSelectedItem();
  }
  
  public boolean isAutorunOn() {
	  if (autorunToggle.isSelected() == true) {
		  return true;
	  }else
		  return false;
  }
	  
  
  
}