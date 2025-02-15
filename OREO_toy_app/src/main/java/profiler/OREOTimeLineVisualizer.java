package profiler;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import timeLine.TimeLine;

public class OREOTimeLineVisualizer {

	public static void main(String[] args) {
		String timelinePath = "./data/multiple-sessions_tl.json";

		TimeLine timeline = getTimeline(timelinePath);
		timeline.print();
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
