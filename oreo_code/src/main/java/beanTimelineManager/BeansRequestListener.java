package beanTimelineManager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import beanTimelineManager.filter.InstanceFilter;
import beanTimelineManager.methodsManager.MethodsCollector;
import timeLine.InstanceMethod;
import timeLine.TimeLine;
import timeLine.TimeLineLogger;

@WebListener
@ApplicationScoped
public class BeansRequestListener implements ServletRequestListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private MethodsCollector methodsCollector;

	private TimeLine timeLine;

	private TimeLineLogger logger;

	private List<Object> initialContextualInstances;

	private LocalTime startRequestTime;

	private LocalTime closeRequestTime;

	@PostConstruct
	public void init() {
		initialContextualInstances = new ArrayList<Object>();
		timeLine = new TimeLine();
		logger = new TimeLineLogger();
	}

	@PreDestroy
	public void close() {
		timeLine.close();
		logger.concludeLog(timeLine);
		persistTimeLine(timeLine);
	}

	private void persistTimeLine(TimeLine timeLine) {
		timeLine.getSessions().remove(0);
		ObjectMapper mapper = new ObjectMapper()
			    .registerModule(new JavaTimeModule())
			    .enable(SerializationFeature.INDENT_OUTPUT)
			    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = now.format(formatter);
		
        try {
			mapper.writeValue(new File("/data/" + formattedDateTime + "_tl.json"), timeLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		startRequestTime = LocalTime.now();
		initialContextualInstances = getLivingContextualInstances();
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		// Deprecated way to obtain sessionID
//		sessionId = idSessionTracker.getSessionId();
//		String _sessionId = sessionId;
//		if (_sessionId == null) {
//			return;
//		}
		closeRequestTime = LocalTime.now();
		ServletRequest servletRequest = event.getServletRequest();
		String sessionId = getSessionID(servletRequest);
		String pageUrl = getPageName(servletRequest);

		// defensive copies
		// TODO could be placed inside the methods
		LocalTime _startRequestTime = startRequestTime;
		LocalTime _closeRequestTime = closeRequestTime;
		List<Object> _initialContextualInstances = new ArrayList<Object>(initialContextualInstances);

		timeLine.concludeCurrentTimeStep(sessionId, _initialContextualInstances);
		List<Object> retrievedContextualInstances = getLivingContextualInstances();
		List<InstanceMethod> observedMethods = methodsCollector.getObservedMethods();
		timeLine.recordNewTimeStep(sessionId, retrievedContextualInstances, observedMethods, _startRequestTime,
				_closeRequestTime, pageUrl);
		// at the end clear the collector
		methodsCollector.ClearCollection();
		logger.log(timeLine);
	}

	private List<Object> getLivingContextualInstances() {
		Predicate<Bean<?>> filter = InstanceFilter.getInstancesToObserveFilter();
		InstanceFinder finder = new InstanceFinder(getManager(), filter);

		List<Object> retrievedContextualInstances = finder.retrieveContextualInstances();
		return retrievedContextualInstances;
	}

	private BeanManager getManager() {
		try {
			return CDI.current().getBeanManager();
		} catch (Exception | LinkageError e) {
			System.err.println("Cannot get BeanManager from CDI.current(); falling back to JNDI. ERROR: " + e);
			try {
				InitialContext ctx = new InitialContext();
				return (BeanManager) ctx.lookup("java:comp/BeanManager");
			} catch (NamingException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	private String getPageName(ServletRequest request) {
		if ((request instanceof HttpServletRequest)) {
			String currentUrl = ((HttpServletRequest) request).getRequestURL().toString();
			return currentUrl;
		}
		return null;
	}

	private String getSessionID(ServletRequest request) {
		if ((request instanceof HttpServletRequest)) {
			String sessionID = ((HttpServletRequest) request).getRequestedSessionId();
			return sessionID;
		}
		return null;
	}

}
