package beanTimelineManager.methodsManager;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.xml.rpc.handler.MessageContext;

import timeLine.InstanceMethod;

@MethodCallsInterceptorBinding
@Interceptor
@Dependent
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
public class MethodCallsInterceptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	private List<Class<?>> involvedClasses;
	private List<Method> involvedMethods;

	@Inject
	MethodsCollector methodsCollector;
	
	public void resetInteractions() {
//		involvedClasses = new ArrayList<Class<?>>();
		involvedMethods = new ArrayList<Method>();
	}

	@AroundInvoke
	public Object manageMethodCall(InvocationContext ctx) throws Exception {

		InstanceMethod method = new InstanceMethod(ctx);
		methodsCollector.addObservedMethod(method);

		try {
			Object result = ctx.proceed();
			return result;
		} catch (Exception e) {
			throw e;
		}
	}

//	public List<Class<?>> getInvolvedClasses() {
//		return involvedClasses;
//	}

	public List<Method> getInvolvedMethods() {
		return involvedMethods;
	}

}
