package profiler;

import java.util.ArrayList;
import java.util.List;

import timeLine.SessionTimeLine;
import timeLine.TimeStep;

public class OreoProfiler {

	private SessionTimeLine targetTimeline;

	public void setTargetTimeline(SessionTimeLine targetTimeline) {
		this.targetTimeline = targetTimeline;
	}

	// TODO

	public List<PropagationTimeLine> forwardPathAnalysis(int initialStepId, String faultyInstanceName,
			int failingStepId, String failedInstanceName) {

		List<TimeStep> stepsToAnalyse = extractStepsOfInterest(initialStepId, failingStepId);
		
		


		return null;
	}

	public List<PropagationTimeLine> forwardPathAnalysis(int initialStepId, String faultyInstanceName,
			int failingStepId) {

		List<TimeStep> stepsToAnalyse = extractStepsOfInterest(initialStepId, failingStepId);
		List<PropagationTimeLine> propagationPaths = new ArrayList<PropagationTimeLine>();
		List<PropagationTimeLine> subPaths = new ArrayList<PropagationTimeLine>();
		
		PropagationTimeLine initialPath = new PropagationTimeLine();

		initialPath.addStep(new PropagationStep(null, null, null));
		subPaths.add(null);
		

		return null;
	}

	private List<TimeStep> extractStepsOfInterest(int initialStepId, int finalStepId) {
		int initialStepIndex = findStepIdIndex(initialStepId);
		int finalStepIndex = findStepIdIndex(finalStepId);
		List<TimeStep> timeSteps = targetTimeline.getTimeSteps();
		return timeSteps.subList(initialStepIndex, finalStepIndex);
	}

	private int findStepIdIndex(int stepId) {
		List<TimeStep> timeSteps = targetTimeline.getTimeSteps();
		int stepNumber = 0;
		while (timeSteps.get(stepNumber).getRequestId() != stepId)
			stepNumber++;
		return stepNumber;
	}

}
