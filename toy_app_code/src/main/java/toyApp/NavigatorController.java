package toyApp;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import beanTimelineManager.methodsManager.MethodCallsInterceptorBinding;

@RequestScoped
@Named
public class NavigatorController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	Conversation conversation;

	public String navigateAndBeginConv(String page) {
		start();
		return navigate(page);
	}
	
	public String navigateAndCloseConv(String page) {
		stop();
		return navigate(page);
	}

	public String navigate(String page) {
		return page + "?faces-redirect=true";
	}

	public void start() {
		if (conversation.isTransient())
			conversation.begin();
	}

	public void stop() {
		if (!conversation.isTransient())
			conversation.end();
	}
}
