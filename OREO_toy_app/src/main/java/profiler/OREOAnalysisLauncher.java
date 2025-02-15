package profiler;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import timeLine.SessionTimeLine;
import timeLine.TimeLine;

public class OREOAnalysisLauncher {

	public static void main(String[] args) {
		String timelinePath = "./data/toyapp_tl.json";
		
		if (args.length > 0) {
            timelinePath = args[0];
		}
        System.out.println("timeline to analyze: " + timelinePath);

		TimeLine timeline = getTimeline(timelinePath);

		try {
			analyzeAllSessions(timeline);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private static void analyzeAllSessions(TimeLine timeline) throws Exception {
		OreoProfiler profiler = new OreoProfiler();
		for (SessionTimeLine sessionTl : timeline.getSessions()) {
			System.out.println();
			System.out.println("Analysis for the session: " + sessionTl.getSessionId());
			System.out.println();

			System.out.println();
			System.out.println("Step-wise failure manifestation analysis for session: " + sessionTl.getSessionId());
			System.out.println();
			profiler.stepWiseFailureManifestationAnalysis(sessionTl);

			System.out.println();
			System.out.println("Step-wise error activation analysis for session: " + sessionTl.getSessionId());
			System.out.println();
			profiler.stepWiseErrorActivationAnalysis(sessionTl);
		}
	}

	private static TimeLine getTimeline(String TLPath) {
		ObjectMapper mapper = getObjectMapper();
		TimeLine loadedTimeline;
		try {
			loadedTimeline = mapper.readValue(new File(TLPath), TimeLine.class);
		} catch (IOException e) {
			loadedTimeline = null;
			e.printStackTrace();
		}
		return loadedTimeline;
	}

	private static ObjectMapper getObjectMapper() {
		return new ObjectMapper().registerModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			    .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
			    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

}
