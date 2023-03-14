package profiler;

import java.util.List;

public class PropagationTimeLine {
	
	private List<PropagationStep> steps;
	

	public List<PropagationStep> getSteps() {
		return steps;
	}
	
	public void addStep(PropagationStep step) {
		steps.add(step);
	}

}
