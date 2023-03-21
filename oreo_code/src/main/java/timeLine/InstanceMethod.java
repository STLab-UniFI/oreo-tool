package timeLine;

import javax.interceptor.InvocationContext;

public class InstanceMethod {

	private String declaringClassName;
	private String methodName;
	private ContextualInstance callerInstance;
	private Object[] parameters;

	public InstanceMethod(String declaringClassName, String methodName) {
		this.declaringClassName = declaringClassName;
		this.methodName = methodName;
	}
	
	public InstanceMethod(InvocationContext ctx) {
//		Object target = ctx.getTarget();
//		System.out.println("oggetto tipo: " + target);
		this.declaringClassName = ctx.getMethod().getDeclaringClass().getName();
		this.methodName = ctx.getMethod().getName();
		this.parameters = ctx.getParameters();
	}

	public String getDeclaringClassName() {
		return declaringClassName;
	}

	public String getMethodName() {
		return methodName;
	}

	public ContextualInstance getCallerInstance() {
		return callerInstance;
	}

	public void setCallerInstance(ContextualInstance callerInstance) {
		this.callerInstance = callerInstance;
	}

	public Object[] getParameters() {
		return parameters;
	}

}
