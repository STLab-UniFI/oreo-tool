package profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timeLine.ContextualInstance;
import timeLine.InstanceMethod;
import timeLine.TimeStep;

public class FEFStep implements Cloneable {

	private TimeStep correspondingSessionStep;
	private List<ContextualInstance> alreadyAffectedInstances;
	private Map<ContextualInstance, InstanceMethod> affectedInstances;
	private List<InstanceMethod> propagations;

	public FEFStep(TimeStep correspondingStep) {
		this.correspondingSessionStep = correspondingStep;
		this.alreadyAffectedInstances = new ArrayList<ContextualInstance>();
		this.affectedInstances = new HashMap<ContextualInstance, InstanceMethod>();
		this.propagations = new ArrayList<InstanceMethod>();
	}

	public TimeStep getCorrespondingSessionStep() {
		return correspondingSessionStep;
	}

	public List<ContextualInstance> propagateFrom(InstanceMethod erroneousMethod) throws Exception { // XXX the list is useless, at least one of the actors is always erroneous
		if (!correspondingSessionStep.getMethodCalls().contains(erroneousMethod))
			throw new Exception("Erroneous Method not found");
		getPropagations().add(erroneousMethod);
		ContextualInstance callee = propagateCallee(erroneousMethod);
		ContextualInstance caller = propagatedCaller(erroneousMethod);

		List<ContextualInstance> affectedInstances = new ArrayList<ContextualInstance>();
		
		if(callee != null)
			affectedInstances.add(callee);
		if (caller != null)
			affectedInstances.add(caller);

		return affectedInstances;
	}


	public ContextualInstance propagateCalleeFrom(InstanceMethod erroneousMethod) throws Exception {
		if (!correspondingSessionStep.getMethodCalls().contains(erroneousMethod))
			throw new Exception("Erroneous Method not found");
		getPropagations().add(erroneousMethod);
		ContextualInstance newlyAffectedInstance = propagateCallee(erroneousMethod);
		return newlyAffectedInstance;
	}
	
	public FEFStep clone() {
		FEFStep clonedStep = new FEFStep(correspondingSessionStep);
		clonedStep.alreadyAffectedInstances = new ArrayList<ContextualInstance>(this.alreadyAffectedInstances);
		clonedStep.affectedInstances = new HashMap<ContextualInstance, InstanceMethod>(this.affectedInstances);
		clonedStep.propagations = new ArrayList<InstanceMethod>(getPropagations());
		return clonedStep;
	}


	private ContextualInstance propagateCallee(InstanceMethod erroneousMethod) {
		ContextualInstance affectedInstance = erroneousMethod.getCallerInstance();
		if (affectedInstance == null)
			return null;
		return manageNewlyAffectedInstance(affectedInstance, erroneousMethod);
	}

	private ContextualInstance propagatedCaller(InstanceMethod erroneousMethod) {
//		ContextualInstance affectedInstance = erroneousMethod.getCallerInstance();
		ContextualInstance affectedInstance = getPreviousMethodInvoked(erroneousMethod).getCallerInstance();
		if (affectedInstance == null)
			return null;
		return manageNewlyAffectedInstance(affectedInstance, erroneousMethod);
	}

	public void markInstanceAsErroneous(ContextualInstance erroneousInstance) throws Exception {
		if(!this.correspondingSessionStep.getLivingInstances().contains(erroneousInstance))
			throw new Exception("Failing Instance not found");
		manageNewlyAffectedInstance(erroneousInstance, null);
	}

	public void markInstanceAsFailed(ContextualInstance failingInstance) throws Exception {
		if(!this.correspondingSessionStep.getLivingInstances().contains(failingInstance))
			throw new Exception("Failing Instance not found");
		manageNewlyAffectedInstance(failingInstance, null);
	}
	
	public ContextualInstance manageNewlyAffectedInstance(ContextualInstance affectedInstance, InstanceMethod erroneousMethod) { // XXX ritorna il parametro in ingresso...
		if(!getAlreadyAffectedInstances().contains(affectedInstance)) {
			getAffectedInstances().put(affectedInstance, erroneousMethod);
			getAlreadyAffectedInstances().add(affectedInstance);
			return affectedInstance;
		}else return null;
	}

	private InstanceMethod getPreviousMethodInvoked(InstanceMethod method) {
		List<InstanceMethod> methodCalls = correspondingSessionStep.getMethodCalls();
        int index = methodCalls.indexOf(method);

        if (index != -1 && index > 0 ) {
            return methodCalls.get(index - 1);
        }
        return null; 
	}

	public boolean isIstancePresent(ContextualInstance newlyAffectedInstance) {
		return correspondingSessionStep.getLivingInstances().contains(newlyAffectedInstance);
	}

	public void updatedInstanceAsAlreadyErroneous(ContextualInstance newlyAffectedInstance) {
		if(isIstancePresent(newlyAffectedInstance))
			getAlreadyAffectedInstances().add(newlyAffectedInstance);
	}

	public boolean methodCouldPropagate(InstanceMethod method) {
		if(correspondingSessionStep.getMethodCalls().indexOf(method) == 0)
			return false;
		
		boolean infectedCallee = getAlreadyAffectedInstances().contains(method.getCallerInstance());
		boolean infectedCaller = getAlreadyAffectedInstances().contains(getPreviousMethodInvoked(method).getCallerInstance());
		if(infectedCaller || infectedCallee) {
			return true;
		}
		else return false;
	}

	public List<ContextualInstance> getAlreadyAffectedInstances() {
		return alreadyAffectedInstances;
	}

	public Map<ContextualInstance, InstanceMethod> getAffectedInstances() {
		return affectedInstances;
	}

	public List<InstanceMethod> getPropagations() {
		return propagations;
	}

}
