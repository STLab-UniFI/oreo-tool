package timeLine;

public class ContextualInstance {
	
	private Object instance;
	private String sessionID;

	public ContextualInstance(Object instance, String sessionID) {
		this.setInstance(instance);
		this.sessionID = sessionID;
	}
	
	public boolean isTheSameInstance(Object instanceToCompare) {
		return this.instance == instanceToCompare;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public String getSessionID() {
		return sessionID;
	}
	
	public String getName() {
//		System.out.println("instance class: " + getInstanceClass());
		return getInstanceClass().toString();
	}

	private Class<?> getInstanceClass() {
		if (isProxy(instance.getClass()))
			return instance.getClass().getSuperclass();
		else
			return instance.getClass();
	}

	private boolean isProxy(Class<?> cls) {
		if (cls.getSimpleName().contains("Proxy$"))
			return true;
		else
			return false;
	}
	
}
