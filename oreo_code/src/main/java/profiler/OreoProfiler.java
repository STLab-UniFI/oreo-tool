package profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import timeLine.ContextualInstance;
import timeLine.InstanceMethod;
import timeLine.SessionTimeLine;
import timeLine.TimeStep;


public class OreoProfiler {

	// Profiling Feature 1
	public Map<String, Map<ContextualInstance, TimeStep>> calculateInstanceSafety(SessionTimeLine timeline,
			TimeStep erroneousTimeStep, InstanceMethod erroneousMethod) throws Exception {
		FEFChain worstFEF = generateWorstCaseFEFChainForward(timeline, erroneousTimeStep, erroneousMethod);

		Map<ContextualInstance, TimeStep> unsafeInstancesWithInfections = worstFEF.retrieveAllInvolvedInstances();
		Map<ContextualInstance, TimeStep> allTimelineInstances = retrieveAllTimelineInstances(timeline);

		Set<ContextualInstance> unsafeSet = unsafeInstancesWithInfections.keySet();
		Map<ContextualInstance, TimeStep> unsafeInstances = unsafeSet.stream()
				.collect(Collectors.toMap(key -> key, allTimelineInstances::get));

		Set<ContextualInstance> safeSet = allTimelineInstances.keySet();
		safeSet.removeAll(unsafeSet);
		Map<ContextualInstance, TimeStep> safeInstances = safeSet.stream()
				.collect(Collectors.toMap(key -> key, allTimelineInstances::get));

		Map<String, Map<ContextualInstance, TimeStep>> results = new HashMap<String, Map<ContextualInstance, TimeStep>>();
		results.put("unsafe", unsafeInstances);
		results.put("safe", safeInstances);

		return results;
	}

	// Profiling Feature 1
	public Map<String, Map<ContextualInstance, TimeStep>> calculateInstanceSafety(SessionTimeLine timeline,
			TimeStep erroneousTimeStep, ContextualInstance erroneousInstance) throws Exception {
		FEFChain worstFEF = generateWorstCaseFEFChainForward(timeline, erroneousTimeStep, erroneousInstance);
		Map<ContextualInstance, TimeStep> unsafeInstancesWithInfections = worstFEF.retrieveAllInvolvedInstances();
		Map<ContextualInstance, TimeStep> allTimelineInstances = retrieveAllTimelineInstances(timeline);

		Set<ContextualInstance> unsafeSet = unsafeInstancesWithInfections.keySet();
		Map<ContextualInstance, TimeStep> unsafeInstances = unsafeSet.stream()
				.collect(Collectors.toMap(key -> key, allTimelineInstances::get));

		Set<ContextualInstance> safeSet = allTimelineInstances.keySet();
		safeSet.removeAll(unsafeSet);
		Map<ContextualInstance, TimeStep> safeInstances = safeSet.stream()
				.collect(Collectors.toMap(key -> key, allTimelineInstances::get));

		Map<String, Map<ContextualInstance, TimeStep>> results = new HashMap<String, Map<ContextualInstance, TimeStep>>();
		results.put("unsafe", unsafeInstances);
		results.put("safe", safeInstances);

		return results;
	}

	// Profiling Feature 1
	public Map<String, Map<ContextualInstance, TimeStep>> calculateInstanceSafety(SessionTimeLine timeline,
			TimeStep erroneousTimeStep, ContextualInstance erroneousInstance, TimeStep untilStep) throws Exception {
		FEFChain worstFEF = generateWorstCaseFEFChainForward(timeline, erroneousTimeStep, erroneousInstance);
		Map<ContextualInstance, TimeStep> completeUnsafeInstancesWithInfections = worstFEF
				.retrieveAllInvolvedInstances();

		int untilStepIndex = timeline.getTimeSteps().indexOf(untilStep);

		Map<ContextualInstance, TimeStep> unsafeInstancesWithInfections = completeUnsafeInstancesWithInfections.entrySet().stream()
				.filter(entry -> timeline.getTimeSteps().indexOf(entry.getValue()) <= untilStepIndex)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		Map<ContextualInstance, TimeStep> allTimelineInstances = retrieveAllTimelineInstances(timeline, untilStepIndex);

		Set<ContextualInstance> unsafeSet = unsafeInstancesWithInfections.keySet();
		Map<ContextualInstance, TimeStep> unsafeInstances = unsafeSet.stream()
				.collect(Collectors.toMap(key -> key, allTimelineInstances::get));

		Set<ContextualInstance> safeSet = allTimelineInstances.keySet();
		safeSet.removeAll(unsafeSet);
		Map<ContextualInstance, TimeStep> safeInstances = safeSet.stream()
				.collect(Collectors.toMap(key -> key, allTimelineInstances::get));

		Map<String, Map<ContextualInstance, TimeStep>> results = new HashMap<String, Map<ContextualInstance, TimeStep>>();
		results.put("unsafe", unsafeInstances);
		results.put("safe", safeInstances);

		return results;
	}

	// Profiling Feature 1
	// XXX instanceName is useless
	public Map<String, Map<ContextualInstance, TimeStep>> calculateInstanceSafety(SessionTimeLine timeline,
			int timeStepNumber, String erroneousInstanceName, String erroneousMethodName) throws Exception {

		TimeStep erroneousTimeStep = timeline.getTimeSteps().get(timeStepNumber);
		ContextualInstance erroneousInstance = retrieveContextualInstanceFromName(erroneousTimeStep,
				erroneousInstanceName);
		if (erroneousInstance == null)
			throw new Exception("No instance with name" + erroneousInstanceName + " found");
		InstanceMethod erroneousMethod = retrieveInstanceMethodFromName(erroneousTimeStep, erroneousMethodName);
		if (erroneousMethod == null)
			throw new Exception("No method with name" + erroneousMethodName + " found");

		return calculateInstanceSafety(timeline, erroneousTimeStep, erroneousMethod);
	}

	// Profiling Feature 1
	public Map<String, Map<ContextualInstance, TimeStep>> calculateInstanceSafety(SessionTimeLine timeline,
			int timeStepNumber, String erroneousInstanceName) throws Exception {

		TimeStep erroneousTimeStep = timeline.getTimeSteps().get(timeStepNumber);
		ContextualInstance erroneousInstance = retrieveContextualInstanceFromName(erroneousTimeStep,
				erroneousInstanceName);
		if (erroneousInstance == null)
			throw new Exception("No instance with name" + erroneousInstanceName + " found");

		return calculateInstanceSafety(timeline, erroneousTimeStep, erroneousInstance);
	}

	// Profiling Feature 2
	public Map<ContextualInstance, TimeStep> calculateRootInstancesFromFailure(SessionTimeLine timeline,
			TimeStep failingTimeStep, ContextualInstance failingInstance) throws Exception {
		FEFChain worstFEF = generateWorstCaseFEFChainBackward(timeline, failingTimeStep, failingInstance);

		Map<ContextualInstance, TimeStep> unsafeInstancesWithInfections = worstFEF.retrieveAllInvolvedInstances();
		Map<ContextualInstance, TimeStep> allTimelineInstances = retrieveAllTimelineInstances(timeline);

		Set<ContextualInstance> unsafeSet = unsafeInstancesWithInfections.keySet();
		Map<ContextualInstance, TimeStep> rootInstances = unsafeSet.stream()
				.collect(Collectors.toMap(key -> key, allTimelineInstances::get));

		return rootInstances;
	}

	// Profiling Feature 2
	public Map<ContextualInstance, TimeStep> calculateRootInstancesFromFailure(SessionTimeLine timeline,
			int timeStepNumber, String failingInstanceName) throws Exception {
		TimeStep failingTimeStep = timeline.getTimeSteps().get(timeStepNumber);
		ContextualInstance failingInstance = retrieveContextualInstanceFromName(failingTimeStep, failingInstanceName);
		if (failingInstance == null)
			throw new Exception("No instance with name" + failingInstanceName + " found");

		return calculateRootInstancesFromFailure(timeline, failingTimeStep, failingInstance);

	}

	// Profiling Feature 3
	public List<FEFChain> generateFEFScenarios(SessionTimeLine timeline, TimeStep faultyTimeStep,
			ContextualInstance faultyInstance) throws Exception {
		FEFChain rootFef = new FEFChain(timeline);
		rootFef.initializePropagationScenario(faultyTimeStep, faultyInstance);
		return rootFef.generateAllPossibleScenariosFromRootError();
	}

	// Profiling Feature 3
	public List<FEFChain> generateFEFScenarios(SessionTimeLine timeline, int timeStepNumber, String faultyInstanceName)
			throws Exception {
		TimeStep faultyTimeStep = timeline.getTimeSteps().get(timeStepNumber);
		ContextualInstance faultyInstance = retrieveContextualInstanceFromName(faultyTimeStep, faultyInstanceName);
		if (faultyInstance == null)
			throw new Exception("No instance with name" + faultyInstanceName + " found");

		return generateFEFScenarios(timeline, faultyTimeStep, faultyInstance);
	}

	// Reproduction of Table 1 of the OREO paper
	public void stepWiseFailureManifestationAnalysis(SessionTimeLine timeline) throws Exception {
		for (int stepIndex = 0; stepIndex < timeline.getTimeSteps().size(); stepIndex++) {
			System.out.println();
			System.out.println("##############");
			System.out.println("## Step: " + (stepIndex+1));
			System.out.println("##############");
			TimeStep step = timeline.getTimeSteps().get(stepIndex);
			for (ContextualInstance instance : step.getLivingInstances()) {
				System.out.println("-  Instance: " + instance.getName());
				Map<ContextualInstance, TimeStep> roots = calculateRootInstancesFromFailure(timeline, step, instance);
				System.out.println("	Root Candidates: " + roots.size());
				Set<ContextualInstance> safeInstances = new HashSet<ContextualInstance>();
				Set<ContextualInstance> unsafeInstances = new HashSet<ContextualInstance>();
				for (ContextualInstance root : roots.keySet()) {
					Map<String, Map<ContextualInstance, TimeStep>> safetyMap = calculateInstanceSafety(timeline,
							roots.get(root), root, step); 
//					Map<ContextualInstance, TimeStep> safeMap = safetyMap.get("safe");
					Map<ContextualInstance, TimeStep> unsafeMap = safetyMap.get("unsafe");
//					safeInstances.addAll(safeMap.keySet());
					unsafeInstances.addAll(unsafeMap.keySet());
				}
				Set<ContextualInstance> alltimelineInstances = retrieveAllTimelineInstances(timeline, stepIndex).keySet(); 

				// ADDED
				safeInstances = new HashSet<ContextualInstance>(alltimelineInstances);
				safeInstances.removeAll(unsafeInstances);

				int numOfUnsafeInstances = unsafeInstances.size();
				double unsafePercentage = (double) numOfUnsafeInstances / alltimelineInstances.size() * 100;
//				/alltimelineInstances.size() *100;
				System.out.println("	Unsafe instances percentage: " + unsafePercentage);

				List<ContextualInstance> currentlyLivingInstances = step.getLivingInstances();

				ArrayList<ContextualInstance> currentlySafeInstances = new ArrayList<ContextualInstance>(
						currentlyLivingInstances);
				currentlySafeInstances.removeAll(unsafeInstances);

				ArrayList<ContextualInstance> currentlyUnsafeInstances = new ArrayList<ContextualInstance>(
						currentlyLivingInstances);
				currentlyUnsafeInstances.removeAll(safeInstances);

				double currentlySafePerc = (double) currentlySafeInstances.size() / currentlyLivingInstances.size() * 100;
				double currentlyUnsafePerc = (double) currentlyUnsafeInstances.size() / currentlyLivingInstances.size() * 100;
				System.out.println("	Currently safe instances percentage: " + currentlySafePerc);
				System.out.println("	Currently unsafe instances percentage: " + currentlyUnsafePerc);
				System.out.println();
			}
		}

	}

	// Reproduction of Table 2 of the OREO Paper
	public void stepWiseErrorActivationAnalysis(SessionTimeLine timeline) throws Exception {
		Map<ContextualInstance, TimeStep> allTimelineInstances = retrieveAllTimelineInstances(timeline);
		for(ContextualInstance instance : allTimelineInstances.keySet()) {
			System.out.println("##############");
			System.out.println("Component Type: " + instance.getName());
			System.out.println("##############");
			TimeStep step = allTimelineInstances.get(instance);
			while (step.getLivingInstances().contains(instance)) {
				int stepIndex = timeline.getTimeSteps().indexOf(step);
				System.out.println("- Step: #" + (stepIndex + 1) );

				List<FEFChain> fefScenarios = generateFEFScenarios(timeline, step, instance);
				System.out.println("	Number of Paths " + fefScenarios.size());
				FEFChain worstCaseFEFChainForward = generateWorstCaseFEFChainForward(timeline, step, instance);
				System.out.println("	Total Errors Worst Case " + worstCaseFEFChainForward.retrieveAllInvolvedInstances().size());
				// Next Step
				if(stepIndex == -1 || stepIndex >= timeline.getTimeSteps().size() -1 )
					break;
				step = timeline.getTimeSteps().get(stepIndex + 1);
			}
		}
	}

	private InstanceMethod retrieveInstanceMethodFromName(TimeStep step, String methodName) {
		for (InstanceMethod method : step.getMethodCalls()) {
			if (method.getMethodName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}

	private ContextualInstance retrieveContextualInstanceFromName(TimeStep step, String contextualInstanceName) {
		for (ContextualInstance instance : step.getLivingInstances()) {
			if (instance.getName().equals(contextualInstanceName)) {
				return instance;
			}
		}
		return null;
	}

	private FEFChain generateWorstCaseFEFChainForward(SessionTimeLine timeline, TimeStep erroneousTimeStep,
			InstanceMethod erroneousMethod) throws Exception {
		FEFChain fef = new FEFChain(timeline);
		fef.initializePropagationScenario(erroneousTimeStep, erroneousMethod);
		fef.worstCasePropagation();
		return fef;
	}

	private FEFChain generateWorstCaseFEFChainForward(SessionTimeLine timeline, TimeStep erroneousTimeStep,
			ContextualInstance erroneousInstance) throws Exception {
		FEFChain fef = new FEFChain(timeline);
		fef.initializePropagationScenario(erroneousTimeStep, erroneousInstance);
		fef.worstCasePropagation();
		return fef;
	}

	private FEFChain generateWorstCaseFEFChainBackward(SessionTimeLine timeline, TimeStep failingTimeStep,
			ContextualInstance failingInstance) throws Exception {
		FEFChain fef = new FEFChain(timeline);
		fef.initializeBackwardPropagationScenario(failingTimeStep, failingInstance);
		fef.worstCasePropagationBackward();
		return fef;
	}

	// XXX this method should be in timeline... however is implemented here to
	// maintain timeline as generic as possible
	private Map<ContextualInstance, TimeStep> retrieveAllTimelineInstances(SessionTimeLine timeline) {
		Map<ContextualInstance, TimeStep> timelineInstances = new HashMap<ContextualInstance, TimeStep>();
		for (TimeStep step : timeline.getTimeSteps()) {
			for (ContextualInstance instance : step.getGeneratedInstances()) {
				timelineInstances.put(instance, step);
			}
		}
		return timelineInstances;
	}

	private Map<ContextualInstance, TimeStep> retrieveAllTimelineInstances(SessionTimeLine timeline,
			int untilStepIndex) {
		Map<ContextualInstance, TimeStep> timelineInstances = new HashMap<ContextualInstance, TimeStep>();
		for(int stepIndex = 0; stepIndex <= untilStepIndex; stepIndex++) {
			TimeStep step = timeline.getTimeSteps().get(stepIndex);
			for (ContextualInstance instance : step.getGeneratedInstances()) {
				timelineInstances.put(instance, step);
			}
		}
		return timelineInstances;
	}

}
