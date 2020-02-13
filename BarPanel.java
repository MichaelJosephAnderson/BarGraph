package barGraph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

public class BarPanel extends JPanel {

  // constant
  private static final int STEPHEIGHT = 10; // height of step markers
  
  // fields
  private ArrayList<Bar> bars;
  private ArrayList<Step> steps;
  private Bar[] barsBuffer;
  private LayoutPanel layoutP;
  private int stepCounter;
  private static final int MAXBARS = 10000;
  
  // constructor
  public BarPanel(LayoutPanel lp)
  {
    this.layoutP = lp;
    this.bars = new ArrayList<Bar>();
    this.steps = new ArrayList<Step>();
    stepCounter = -1;
    this.barsBuffer = new Bar[MAXBARS];
  }
  
  // methods
  public void addBar()
  {
    // create new bar
    int screenHeight = this.getHeight();
    // random value = Math.random() * range of values + min value
    int barHeight = (int) (Math.random() * (screenHeight - 50) + 50);
    Bar b = new Bar(barHeight);
    bars.add(b);
    this.repaint();
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    if (bars.size() == 0)
    {
      return;
    }
    
    int barWidth, totalBarWidth, startX;
    barWidth = this.getBarWidth();
    totalBarWidth = barWidth * bars.size();
    startX = (this.getWidth() - totalBarWidth) / 2;
    // looping through all bars in bars array
    for (int index = 0; index < bars.size(); index++)
    {
      Bar b = bars.get(index);
      g.setColor(b.getColor());
      // to draw bar, the top left corner has startX for x-coord, screen Height - barHeight for y-coord
      g.fillRect(getBarX(index), this.getHeight() - b.getHeight(), barWidth, b.getHeight()); 
    }
    
    drawBuffer(g);
    
    this.drawStepMark(g);
    
    /* alternatively:
     * for (int i = 0; i < bars.size(); i++)
     *   Bar b = bars.get(i);
     */
  }
  
  private void swapBars(int index1, int index2)
  {
    Bar a = bars.get(index1);
    Bar b = bars.get(index2);
    bars.set(index1, b);
    bars.set(index2, a);
  }
  
  public void randomize()
  {
    for (int i = 0; i < bars.size(); i++)
    {
      // swap bar at index i with random bar in array
      int randIndex = (int) (Math.random() * bars.size());
      swapBars(i, randIndex);
    }
    this.repaint();
  }
  
  public void bubbleSort()
  {
    if (bars.size() == 0)
      return;
    this.clearSteps();
    
    int index1, index2, endIndex;
    // outer loop changes the last index (where sorted portion delimited)
    for (endIndex = bars.size() - 1; endIndex > 0; endIndex--)
    {
      // inner loop will do one pass through of all unsorted elements
      for (index1 = 0; index1 <= endIndex - 1; index1++)
      {
        index2 = index1 + 1;
        Bar a = bars.get(index1);
        Bar b = bars.get(index2);
        Step compare = new Step(index1, index2, Step.COMPARE);
        steps.add(compare);
        if (a.getHeight() > b.getHeight()) {// if left bar is bigger right bar
          Step swap = new Step(index1, index2, Step.SWAP);
          steps.add(swap);
          this.swapBars(index1, index2);
        }
      }
    }
    stepCounter = steps.size();
    while (stepCounter != 0)
      this.stepBack();
    this.repaint();
  }
  
  public int getBarWidth() { 
    int width = this.getWidth() / bars.size();
    if (width > 300)
      return 300;
    else return width;
  }
  
  // return the x-coordinate of bar drawn at index
  public int getBarX(int index) {
    int barWidth, totalBarWidth, startX;
    barWidth = this.getBarWidth();
    totalBarWidth = barWidth * bars.size();
    startX = (this.getWidth() - totalBarWidth) / 2;
    int x = startX + barWidth * index;
    return x;
  }
  
  public void drawStepMark(Graphics g)
  {
    if (stepCounter == -1 || stepCounter == steps.size()) {
      return;
    }
    int halfBar = getBarWidth()/2;
    Step currentStep = steps.get(stepCounter);
    int x1 = getBarX(currentStep.getIndex1());
    int x2 = getBarX(currentStep.getIndex2());
    if (currentStep.getType() == Step.STORETOBUFFER) {
    	g.setColor(Color.CYAN);
    	g.fillRect(x1 + halfBar, this.getHeight() - STEPHEIGHT, halfBar, STEPHEIGHT);
    	g.setColor(Color.YELLOW);
    	g.fillRect(x2, this.getHeight() - STEPHEIGHT, halfBar, STEPHEIGHT);
    }
    if (currentStep.getType() == Step.SAVEFROMBUFFER) {
    	g.setColor(Color.YELLOW);
    	g.fillRect(x1, this.getHeight() - STEPHEIGHT, halfBar, STEPHEIGHT);
    	g.setColor(Color.BLUE);
    	g.fillRect(x2 + halfBar, this.getHeight() - STEPHEIGHT, halfBar, STEPHEIGHT);
    }
    
    if (currentStep.getType() == Step.COMPARE) {
      g.setColor(Color.YELLOW);
    } else if (currentStep.getType() == Step.SWAP) {
      g.setColor(Color.RED);
    }
    g.fillRect(x1, this.getHeight() - STEPHEIGHT, getBarWidth(), STEPHEIGHT);
    g.fillRect(x2, this.getHeight() - STEPHEIGHT, getBarWidth(), STEPHEIGHT);
  }
  
  public void drawBuffer(Graphics g) {
	  for (int i = 0; i < MAXBARS; i++) {
		  Bar bar = barsBuffer[i];
		  if (bar != null) {
			 g.setColor(getTranslucentColor(bar.getColor()));
			 g.fillRect(this.getBarX(i), this.getHeight() - STEPHEIGHT - bar.getHeight(), this.getBarWidth(), bar.getHeight());
			 g.setColor(Color.BLACK);
			 g.drawRect(this.getBarX(i), this.getHeight() - STEPHEIGHT - bar.getHeight(), this.getBarWidth(), bar.getHeight());
		  }
	  }
  }
  
  public Color getTranslucentColor(Color c) { 
	  return new Color(c.getRed(), c.getBlue(), c.getGreen(), 50); 
  }

  
  public void clearSteps()
  {
    steps.clear();
    stepCounter = -1;
  }
  
  public void stepBack()
  {
    if (stepCounter == 0)
    {
      return;
    }
    stepCounter--;
    Step s = steps.get(stepCounter);
    if (s.getType() == Step.SWAP) {
      this.swapBars(s.getIndex1(), s.getIndex2());
    }
    if (s.getType() == Step.STORETOBUFFER) {
    	this.saveBar(s.getIndex2(), s.getIndex1());
    }
    if (s.getType() == Step.SAVEFROMBUFFER) {
    	this.storeBar(s.getIndex2(), s.getIndex1());
    }
    repaint();
  }
  
  public void stepForward()
  {
    if (stepCounter == -1 || stepCounter == steps.size())
      return;
    Step s = steps.get(stepCounter);
    if (s.getType() == Step.SWAP) {
      this.swapBars(s.getIndex1(), s.getIndex2());
    }
    if (s.getType() == Step.STORETOBUFFER) {
    	this.storeBar(s.getIndex1(), s.getIndex2());
    }
    if (s.getType() == Step.SAVEFROMBUFFER) {
    	this.saveBar(s.getIndex1(), s.getIndex2());
    }
    repaint();
    stepCounter++;
  }

  public void selectionSort() {
	if (bars.size() == 0) {
		return;
	}
	this.clearSteps();
	
	for (int j = 0; j < bars.size() -1; j++) {
		
		//inner loop-find min values and swap
		int minValue = bars.get(j).getHeight(); // min should start with the first value of the unsorted portion
		int minIndex = j;
		for(int k = j; k < bars.size(); k++) {
			Step compare = new Step(j, k, Step.COMPARE);
			steps.add(compare);
			//check to see if k values is smaller, if so update minValue
			if (minValue > bars.get(k).getHeight()) {
				minValue = bars.get(k).getHeight();
				minIndex = k;
			}
		}
		//swap min index with first unsorted index
		if (j != minIndex) {
			Step swap = new Step(j, minIndex, Step.SWAP);
			steps.add(swap);
			this.swapBars(j, minIndex);
		}
	}
	stepCounter = steps.size();
    while (stepCounter != 0)
      this.stepBack();
    this.repaint();
	
}

  public void insertionSort() {
		if (bars.size() == 0) {
			return;
		}
		this.clearSteps();
		
		for (int j = 1; j < bars.size(); j++) {
			
			Bar insertValue = bars.get(j);
			for (int k = j; k > 0; k--) {
				Step compare = new Step(k-1, k, Step.COMPARE);
				steps.add(compare);
				//compare consecutive elements, if necessary, swap
				if (insertValue.getHeight() < bars.get(k-1).getHeight()) {
					Step swap = new Step(k-1, k, Step.SWAP);
					steps.add(swap);
					bars.set(k, bars.get(k-1));
					bars.set(k-1, insertValue);
				}else {
					break;
				}
			}
		}
		
		stepCounter = steps.size();
	    while (stepCounter != 0)
	      this.stepBack();
	    this.repaint();
		
	}
	
  public void storeBar(int fromBarIndex, int toBufferIndex) {
	  barsBuffer[toBufferIndex] = bars.get(fromBarIndex);
  }
  
  public void saveBar(int fromBufferIndex, int toBarIndex) {
	 // if (barsBuffer[fromBufferIndex] == null) {
		 // System.out.println("Bad Save");
		 // System.out.println(1/0);
	 // }
	  bars.set(toBarIndex, barsBuffer[fromBufferIndex]);
	  barsBuffer[fromBufferIndex] = null;
  }
  
  public void recordStore(int fromBarIndex, int toBufferIndex) {
	  storeBar(fromBarIndex, toBufferIndex);
	  Step STORE = new Step(fromBarIndex, toBufferIndex, Step.STORETOBUFFER);
	  steps.add(STORE);
  }
  
  public void recordSave(int fromBufferIndex, int toBarIndex) {
	  saveBar(fromBufferIndex, toBarIndex);
	  Step STORE = new Step(fromBufferIndex, toBarIndex, Step.SAVEFROMBUFFER);
	  steps.add(STORE);
  }
  
  public void mergeSort() {
	  if (bars.size() == 0) {
		  return;
	  }
	  clearSteps();
	  mergeSort(0, bars.size() - 1);
	  stepCounter = steps.size();
	  //stepCounter = 0;
	  while (stepCounter != 0) {
		  System.out.println("reversing step #: " + stepCounter);
		  stepBack();
	  }
	  repaint();
  }
  
  public void mergeSort(int lowIndex, int highIndex) {
		if (lowIndex == highIndex) {
			return;
		}else if (highIndex == lowIndex + 1) {
			Step COMPARE = new Step(lowIndex, highIndex, Step.COMPARE);
			steps.add(COMPARE);
			if (bars.get(lowIndex).getHeight() > bars.get(highIndex).getHeight()) {
				Step SWAP = new Step(lowIndex, highIndex, Step.SWAP);
				steps.add(SWAP);
				this.swapBars(lowIndex, highIndex);
			}
			return;
		}
		
		int midIndex = (lowIndex + highIndex) / 2;
		
		mergeSort(lowIndex, midIndex);
		mergeSort(midIndex + 1, highIndex);
		
		int storeIndex = lowIndex;
		int bottomIndex = lowIndex;
		int topIndex = midIndex + 1;
		
		while(storeIndex <= highIndex) {
			if (bottomIndex == midIndex + 1) {
				recordStore(topIndex, storeIndex);
				topIndex++;
				storeIndex++;
			}else if (topIndex == highIndex + 1) {
				recordStore(bottomIndex, storeIndex);
				bottomIndex++;
				storeIndex++;
			}else {
				Step COMPARE = new Step(topIndex, bottomIndex, Step.COMPARE);
				steps.add(COMPARE);
				if (bars.get(bottomIndex).getHeight() < bars.get(topIndex).getHeight()) {
					recordStore(bottomIndex, storeIndex);
					bottomIndex++;
					storeIndex++;
				}else {
					recordStore(topIndex, storeIndex);
					topIndex++;
					storeIndex++;
				}
			}
				
		}
		
		for (int index = lowIndex; index <= highIndex; index++) {
			recordSave(index, index);
		}
		
		
	}


}
