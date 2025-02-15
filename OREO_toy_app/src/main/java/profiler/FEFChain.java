package profiler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import timeLine.ContextualInstance;
import timeLine.InstanceMethod;
import timeLine.SessionTimeLine;
import timeLine.TimeStep;


public class FEFChain {

	private String FEFid;
	private List<FEFStep> timesteps;
	private FEFStep rootStep;
	private InstanceMethod triggerMethod;
	private ContextualInstance rootInstance;
	private FEFStep failedStep;
	private ContextualInstance failedInstance;

	public FEFChain(SessionTimeLine timeline) {
		this.timesteps = new ArrayList<FEFStep>();
		this.FEFid = timeline.getSessionId();
		for (TimeStep step : timeline.getTimeSteps()) {
			getTimesteps().add(new FEFStep(step));
		}
	}

	public FEFChain(FEFChain fefChain) {
		this.FEFid = fefChain.FEFid;
		this.timesteps = new ArrayList<FEFStep>();
		for (FEFStep step : fefChain.timesteps) {
			this.timesteps.add(step.clone());
		}
		this.rootStep = fefChain.rootStep;
		this.triggerMethod = fefChain.triggerMethod;
		this.rootInstance = fefChain.rootInstance;
		this.failedStep = fefChain.failedStep;
		this.failedInstance = fefChain.failedInstance;
	}

	public void initializePropagationScenario(TimeStep erroneousTimeStep, InstanceMethod erroneousMethod)
			throws Exception {
		FEFStep fefStep = retrieveFEFStep(erroneousTimeStep);
		if (fefStep == null)
			throw new Exception("Time Step not found");
		ContextualInstance newlyAffectedInstance = propagatesToCalleeFrom(fefStep, erroneousMethod);
		if (newlyAffectedInstance != null) {
			this.rootInstance = newlyAffectedInstance;
			this.triggerMethod = erroneousMethod;
			this.rootStep = fefStep;
		}
	}

	public void initializePropagationScenario(TimeStep erroneousTimeStep, ContextualInstance erroneousInstance)
			throws Exception {
		FEFStep fefStep = retrieveFEFStep(erroneousTimeStep);
		if (fefStep == null)
			throw new Exception("Time Step not found");
		fefStep.markInstanceAsErroneous(erroneousInstance);
		forwardPropagation(fefStep, erroneousInstance);
		this.rootInstance = erroneousInstance;
		this.rootStep = fefStep;
	}

	public void initializeBackwardPropagationScenario(TimeStep failingTimeStep, ContextualInstance failingInstance)
			throws Exception {
		FEFStep fefStep = retrieveFEFStep(failingTimeStep);
		if (fefStep == null)
			throw new Exception("Time Step not found");

		fefStep.markInstanceAsFailed(failingInstance);
		backwardPropagation(fefStep, failingInstance);
		this.failedStep = fefStep;
		this.failedInstance = failingInstance;

	}

	public List<FEFChain> generateAllPossibleScenariosFromRootError() throws Exception {
		if (this.getRootStep() == null || this.getRootInstance() == null)
			throw new Exception("Chain not properly intialized");

		return generateAllPossibleScenariosStrartingFromSpecificStepNumber(timesteps.indexOf(rootStep));
	}

//	public List<FEFChain> generateAllPossibleScenariosFromSpecificStepNumber(int stepNumber) throws Exception {
//		List<FEFChain> scenarios = new ArrayList<FEFChain>();
//		scenarios.add(this);
//		FEFStep specificStep = timesteps.get(stepNumber);
//		for (InstanceMethod method : specificStep.getCorrespondingSessionStep().getMethodCalls())
//			for (FEFChain scenario : scenarios) {
//				if (specificStep.methodCouldPropagate(method)) {
//					FEFChain newScenario = new FEFChain(scenario);
//
//					scenarios.add(newScenario);
//
//					// happyScenario
//					// scenarios.addAll();
//
//				}
//
//			}
//
//		return scenarios;
//
//	}

//	public List<FEFChain> generateAllPossibleScenariosStrartingFromSpecificStepNumber(int stepNumber) throws Exception {
//		List<FEFChain> scenarios = new ArrayList<FEFChain>();
//		scenarios.add(this);
//		for (int stepIndex = stepNumber; stepIndex < timesteps.size(); stepIndex++)
//			for (int methodIndex = 0; methodIndex < timesteps.get(stepNumber).getCorrespondingSessionStep()
//					.getMethodCalls().size(); methodIndex++) {
//				for (FEFChain scenario : scenarios) {
//					FEFChain newScenario = new FEFChain(scenario);
//					FEFStep step = newScenario.timesteps.get(stepIndex);
//					InstanceMethod method = step.getCorrespondingSessionStep().getMethodCalls().get(methodIndex);
//					if(newScenario.propagateFrom(step, method))
//						scenarios.add(newScenario);
//				}
//			}
//		return scenarios;
//	}

//	public List<FEFChain> generateAllPossibleScenariosStrartingFromSpecificStepNumber(int stepNumber) throws Exception {
//		List<FEFChain> scenarios = new ArrayList<FEFChain>();
//		scenarios.add(this);
//		for (int stepIndex = stepNumber; stepIndex < timesteps.size(); stepIndex++)
//			for (int methodIndex = 0; methodIndex < timesteps.get(stepNumber).getCorrespondingSessionStep()
//					.getMethodCalls().size(); methodIndex++) {
//				for (int scenarioIndex = 0; scenarioIndex < scenarios.size(); scenarioIndex++) {
//					FEFChain newScenario = new FEFChain(scenarios.get(scenarioIndex));
//					FEFStep step = newScenario.timesteps.get(stepIndex);
//					InstanceMethod method = step.getCorrespondingSessionStep().getMethodCalls().get(methodIndex);
//					if (newScenario.propagateFrom(step, method))
//						scenarios.add(newScenario);
//				}
//			}
//		return scenarios;
//	}

	public List<FEFChain> generateAllPossibleScenariosStrartingFromSpecificStepNumber(int stepNumber) throws Exception {
		List<FEFChain> scenarios = new ArrayList<FEFChain>();
		scenarios.add(this);
		for (int stepIndex = stepNumber; stepIndex < timesteps.size(); stepIndex++) {
			scenarios.addAll(generateAllPossibleScenariosInSingleStep(stepIndex, scenarios));
		}
		return scenarios;
	}

	private List<FEFChain> generateAllPossibleScenariosInSingleStep(int stepNumber, List<FEFChain> scenarios)
			throws Exception {

		List<FEFChain> newScenarios = new ArrayList<FEFChain>();
		for (FEFChain scenario : scenarios) {
			newScenarios.addAll(scenario.genereateAllPossibleScenariosInSingleStep(stepNumber));
		}
		return newScenarios;

//		for (int methodIndex = 0; methodIndex < timesteps.get(stepNumber).getCorrespondingSessionStep().getMethodCalls()
//				.size(); methodIndex++) {
//			for (int scenarioIndex = 0; scenarioIndex < scenarios.size(); scenarioIndex++) {
//				FEFChain newScenario = new FEFChain(scenarios.get(scenarioIndex));
//				FEFStep step = newScenario.timesteps.get(stepNumber);
//				InstanceMethod method = step.getCorrespondingSessionStep().getMethodCalls().get(methodIndex);
//				if (newScenario.propagateFrom(step, method))
//					scenarios.add(newScenario);
//			}
//		}
//		return null;
	}

//	private List<FEFChain> genereateAllPossibleScenariosInSingleStep(int stepNumber) {
//		List<FEFChain> newScenarios = new ArrayList<FEFChain>();
//		for (int methodIndex = 0; methodIndex < timesteps.get(stepNumber).getCorrespondingSessionStep().getMethodCalls()
//				.size(); methodIndex++) {
//			FEFChain newScenario = new FEFChain(this);
//			FEFStep step = newScenario.timesteps.get(stepNumber);
//			InstanceMethod method = step.getCorrespondingSessionStep().getMethodCalls().get(methodIndex);
//
//		}
//
//		return newScenarios;
//	}

	private List<FEFChain> genereateAllPossibleScenariosInSingleStep(int stepNumber) throws Exception {
		List<List<Integer>> rawPowerSetIndices = getPowerSetIndices(
				timesteps.get(stepNumber).getCorrespondingSessionStep().getMethodCalls().size() - 1); // first method
																										// does not
																										// count
		List<List<Integer>> powerSetIndices = rawPowerSetIndices.stream()
				.map(innerList -> innerList.stream().map(i -> i + 1).collect(Collectors.toList()))
				.collect(Collectors.toList());

		List<FEFChain> scenarios = new ArrayList<FEFChain>();

		for (List<Integer> set : powerSetIndices) {
			if (!set.isEmpty()) {
				FEFChain scenario = generateFEFChainFromIndicesSet(stepNumber, set);
				if (scenario != null)
					scenarios.add(scenario);
			}
		}
		return scenarios;
	}

	private FEFChain generateFEFChainFromIndicesSet(int stepNumber, List<Integer> set) throws Exception {
		FEFChain scenario = new FEFChain(this);
		List<InstanceMethod> methodCalls = scenario.timesteps.get(stepNumber).getCorrespondingSessionStep()
				.getMethodCalls();
		FEFStep fefStep = scenario.timesteps.get(stepNumber);
		for (Integer methodIndex : set) {
			scenario.propagateFrom(fefStep, methodCalls.get(methodIndex));
		}
		if (scenario.getNumberOfTotalPropagations() == this.getNumberOfTotalPropagations() + set.size())
			return scenario;
		else
			return null;
	}

	private List<List<Integer>> getPowerSetIndices(int setSize) {
		List<List<Integer>> result = new ArrayList<>();
		backtrack(setSize, 0, new ArrayList<>(), result);
		return result;
	}

	private void backtrack(int size, int start, List<Integer> tempList, List<List<Integer>> result) {
		result.add(new ArrayList<>(tempList));
		for (int i = start; i < size; i++) {
			tempList.add(i);
			backtrack(size, i + 1, tempList, result);
			tempList.remove(tempList.size() - 1);
		}
	}

	private ContextualInstance propagatesToCalleeFrom(FEFStep step, InstanceMethod method) throws Exception {
		ContextualInstance newlyAffectedInstance = step.propagateCalleeFrom(method);
		if (newlyAffectedInstance != null) {
			forwardPropagation(step, newlyAffectedInstance);
			return newlyAffectedInstance;
		} else
			return null;
	}

	private void forwardPropagation(FEFStep initialStep, ContextualInstance newlyAffectedInstance) {
		ListIterator<FEFStep> stepIterator = timesteps.listIterator(timesteps.indexOf(initialStep) + 1);
		boolean isNewlyAffectedIstancePresent = true;
		while (stepIterator.hasNext() & isNewlyAffectedIstancePresent) {
			FEFStep next = stepIterator.next();
			isNewlyAffectedIstancePresent = next.isIstancePresent(newlyAffectedInstance);
			if (isNewlyAffectedIstancePresent)
				next.updatedInstanceAsAlreadyErroneous(newlyAffectedInstance);
		}
	}

	private void backwardPropagation(FEFStep initialStep, ContextualInstance newlyAffectedInstance) {
		ListIterator<FEFStep> stepIterator = timesteps.listIterator(timesteps.indexOf(initialStep));
		boolean isNewlyAffectedIstancePresent = true;
		while (stepIterator.hasPrevious() & isNewlyAffectedIstancePresent) {
			FEFStep previous = stepIterator.previous();
			isNewlyAffectedIstancePresent = previous.isIstancePresent(newlyAffectedInstance);
			if (isNewlyAffectedIstancePresent)
				previous.updatedInstanceAsAlreadyErroneous(newlyAffectedInstance);
		}

	}

	private FEFStep retrieveFEFStep(TimeStep erroneousTimeStep) {
		for (FEFStep fefStep : timesteps) {
			if (fefStep.getCorrespondingSessionStep().equals(erroneousTimeStep))
				return fefStep;
		}

		return null;
	}

	public void worstCasePropagation() throws Exception {

		List<InstanceMethod> methodCalls = this.getRootStep().getCorrespondingSessionStep().getMethodCalls();

		int indexToStart;
		if(triggerMethod == null) 
			indexToStart = 1;
		else
			indexToStart = methodCalls.indexOf(triggerMethod) + 1;
		ListIterator<InstanceMethod> methodIterator = methodCalls
				.listIterator(indexToStart);

		ListIterator<FEFStep> stepIterator = timesteps.listIterator(timesteps.indexOf(getRootStep()));
		while (stepIterator.hasNext()) {
			FEFStep step = stepIterator.next();
			methodCalls = step.getCorrespondingSessionStep().getMethodCalls();
			methodIterator = methodCalls.listIterator();
			propagatesAllMethods(step, methodIterator);
		}
	}

	private void propagatesAllMethods(FEFStep step, ListIterator<InstanceMethod> methodIterator) throws Exception {
		while (methodIterator.hasNext()) {
			InstanceMethod method = methodIterator.next();
			propagateFrom(step, method);
		}
	}

	private boolean propagateFrom(FEFStep step, InstanceMethod method) throws Exception {
		if (step.methodCouldPropagate(method)) {
			List<ContextualInstance> affectedInstances = step.propagateFrom(method);
			for (ContextualInstance affectedInstance : affectedInstances) {
				forwardPropagation(step, affectedInstance);
			}
			return true;
		}
		return false;
	}

	public void worstCasePropagationBackward() throws Exception {
		List<InstanceMethod> methodCalls;
		ListIterator<InstanceMethod> methodIterator;
		ListIterator<FEFStep> stepIterator = timesteps.listIterator(timesteps.indexOf(getFailedStep()) + 1);
		while (stepIterator.hasPrevious()) {
			FEFStep step = stepIterator.previous();
			methodCalls = step.getCorrespondingSessionStep().getMethodCalls();
			methodIterator = methodCalls.listIterator(methodCalls.size());
			propagatesAllMethodsBackward(step, methodIterator);
		}
	}

	private void propagatesAllMethodsBackward(FEFStep step, ListIterator<InstanceMethod> methodIterator)
			throws Exception {
		while (methodIterator.hasPrevious()) {
			InstanceMethod method = methodIterator.previous();
			if (step.methodCouldPropagate(method)) {
				List<ContextualInstance> affectedInstances = step.propagateFrom(method);
				for (ContextualInstance affectedInstance : affectedInstances) {
					backwardPropagation(step, affectedInstance);
				}
			}
		}

	}

	public Map<ContextualInstance, TimeStep> retrieveAllInvolvedInstances() {
		Map<ContextualInstance, TimeStep> involvedInstances = new HashMap<ContextualInstance, TimeStep>();
		for (FEFStep step : this.timesteps) {
			for (ContextualInstance instance : step.getAffectedInstances().keySet()) {
				involvedInstances.put(instance, step.getCorrespondingSessionStep());
			}
		}
		return involvedInstances;
	}

	public void print() {
		System.out.println("Total Number of Propagations: " + getNumberOfTotalPropagations());
		for (int stepIndex = 0; stepIndex < timesteps.size(); stepIndex++) {
			System.out.println("Step number: " + stepIndex);
			FEFStep step = timesteps.get(stepIndex);
			List<InstanceMethod> propagations = step.getPropagations();
			List<ContextualInstance> alreadyAffectedInstances = step.getAlreadyAffectedInstances();
			System.out.println("Erroneous Instances: ");
			for (ContextualInstance instance : alreadyAffectedInstances) {
				System.out.println(instance.getName());
			}
			System.out.println();
			System.out.println("Propagation in the step:");
			for (InstanceMethod propagation : propagations) {
				System.out.println(propagation.getCallerInstance() + " " + propagation.getMethodName());
			}
			System.out.println();
		}
	}

	public int getNumberOfTotalPropagations() {
		return (int) timesteps.stream().flatMap(step -> step.getPropagations().stream()).count();
	}

	public String getFEFid() {
		return FEFid;
	}

	public FEFStep getRootStep() {
		return rootStep;
	}

	public InstanceMethod getTriggerMethod() {
		return triggerMethod;
	}

	public ContextualInstance getRootInstance() {
		return rootInstance;
	}

	public FEFStep getFailedStep() {
		return failedStep;
	}

	public ContextualInstance getFailedInstance() {
		return failedInstance;
	}

	public List<FEFStep> getTimesteps() {
		return timesteps;
	}

}
