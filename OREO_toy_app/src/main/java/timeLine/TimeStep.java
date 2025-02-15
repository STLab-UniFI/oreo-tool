package timeLine;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uuid", scope = TimeStep.class)
public class TimeStep {

	private final UUID uuid = UUID.randomUUID();
	private String sessionId;
	private int requestId;
	private List<ContextualInstance> livingInstances;

	private List<ContextualInstance> generatedInstances;
	private List<ContextualInstance> destroyedInstances;
	private List<InstanceMethod> methodCalls;
	private LocalTime beginTime;
	private LocalTime endTime;
	private String pageId;

	public TimeStep(int requestId, String sessionId, String pageId) {
		this.requestId = requestId;
		this.sessionId = sessionId;
		this.pageId = pageId;
		this.livingInstances = new ArrayList<ContextualInstance>();
		this.generatedInstances = new ArrayList<ContextualInstance>();
		this.destroyedInstances = new ArrayList<ContextualInstance>();
		this.methodCalls = new ArrayList<InstanceMethod>();
	}

	public TimeStep() {
	}

	public List<ContextualInstance> getLivingInstances() {
		return livingInstances;
	}

	public void setLivingInstances(List<ContextualInstance> instances) {
		// defensive copy
		this.livingInstances = new ArrayList<ContextualInstance>(instances);
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getSessionId() {
		return sessionId;
	}

	public List<ContextualInstance> getGeneratedInstances() {
		return generatedInstances;
	}

	public void setGeneratedInstances(List<ContextualInstance> generatedInstances) {
		this.generatedInstances = generatedInstances;
	}

	public List<ContextualInstance> getDestroyedInstances() {
		return destroyedInstances;
	}

	public void setDestroyedInstances(List<ContextualInstance> destroyedInstances) {
		this.destroyedInstances = destroyedInstances;
	}

	public int getRequestId() {
		return requestId;
	}

	public void addMethodCall(InstanceMethod method) {
		getMethodCalls().add(method);
	}

	public List<InstanceMethod> getMethodCalls() {
		return methodCalls;
	}

	public LocalTime getBeginTime() {
		return beginTime;

	}

	public void setBeginTime(LocalTime beginTime) {
		this.beginTime = beginTime;

	}

	public LocalTime getEndTime() {
		return endTime;

	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;

	}

	public String getPageId() {
		return pageId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeStep other = (TimeStep) obj;
		return Objects.equals(uuid, other.uuid);
	}

	public void print() {
		System.out.println( "################ STEP ID " + requestId + " ################" );
		
		String metaInfo = "# Started at: " + beginTime + '\n' + "# Ended at: " + endTime + '\n'
				+ "#" + '\n';

		metaInfo += "# Page ID: " + pageId + '\n' + "#" + '\n';

		System.out.println(metaInfo);
		
		
		String methods = "# Method calls: (" + methodCalls.size() + ")" + '\n';
		for (InstanceMethod method : methodCalls) {
			methods += "# - " + method.getDeclaringClassName() + "." + method.getMethodName() + "( ";
			for (Object object : method.getParameters()) {
				methods += object.toString() + ", "; 
			}
			methods += ")" + '\n';
			methods += "Caller Instance: " + method.getCallerInstance() + '\n';
		}
		methods += "#" + '\n';
		
		System.out.println(methods);
		
		
		String instances = "# Living instances: (" + livingInstances.size() + ")" + '\n';
		for (ContextualInstance instance : livingInstances) {
			instances += "# - " + instance.getName() + '\n';
		}
		instances += "#" + '\n';

		System.out.println(instances);
		

		String createdInstances = "# Created instances: (" + generatedInstances.size() + ")" + '\n';
		for (ContextualInstance instance : generatedInstances) {
			createdInstances += "# + " + instance.getName() + '\n';
		}
		createdInstances += "#" + '\n';

		System.out.println(createdInstances);


		String destroyedInstancesLog = "# Destroyed instances: (" + destroyedInstances.size() + ")" + '\n';
		for (ContextualInstance instance : destroyedInstances) {
			destroyedInstancesLog += "# x " + instance.getName() + '\n';
		}
		destroyedInstancesLog += "#" + '\n';

		System.out.println(destroyedInstancesLog);
		
		System.out.println("###################### END OF STEP #######################" + '\n');
		System.out.println();
		System.out.println();
		
	}


}
