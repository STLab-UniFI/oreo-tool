package profiler;

import java.time.LocalTime;
import java.util.List;

import timeLine.ContextualInstance;
import timeLine.InstanceMethod;
import timeLine.TimeStep;

@Deprecated
public class PropagationStep {

	private String sessionId;
	private int requestId;
	private String pageId;
	private LocalTime beginTime;
	private LocalTime endTime;

	private List<ContextualInstance> involvedInstances;
	private List<InstanceMethod> propagationMethods;

	public PropagationStep(TimeStep timestep, List<ContextualInstance> intances, List<InstanceMethod> methods) {
		this.sessionId = timestep.getSessionId();
		this.requestId = timestep.getRequestId();
		this.pageId = timestep.getPageId();
		this.beginTime = timestep.getBeginTime();
		this.endTime = timestep.getEndTime();
		this.involvedInstances = intances;
		this.propagationMethods = methods;
	}

	public List<ContextualInstance> getInvolvedInstances() {
		return involvedInstances;
	}

	public void setInvolvedInstances(List<ContextualInstance> involvedInstances) {
		this.involvedInstances = involvedInstances;
	}

	public List<InstanceMethod> getPropagationMethods() {
		return propagationMethods;
	}

	public void setPropagationMethods(List<InstanceMethod> propagationMethods) {
		this.propagationMethods = propagationMethods;
	}
}
