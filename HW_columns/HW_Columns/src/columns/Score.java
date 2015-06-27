package columns;

public class Score {
    static int MaxLevel=7;
    static int FigToDrop=33;	//max number of lines built for level

	long DScore;  //drop bonus
	int k;		  //triplet counter
	int Level;
	long Score;

	void setDropBonus(int rows) {
		DScore = (((Level+1)*(View.Depth*2-rows) * 2) % 5) * 5;
	}

	void addLineBonus() {
		Score += (Level+1)*10;		
	}

	public void updateLineCounter() {
        if (++k>=FigToDrop) {
            k = 0;
            if (Level<MaxLevel) 
            	Level++;		
        }
	}

	public void addDropBonus() {
		Score+=DScore;		
	}

	public long calculateDelay() {
		return (MaxLevel-Level)*Columns.TimeShift+Columns.MinTimeShift;
	}	
}
