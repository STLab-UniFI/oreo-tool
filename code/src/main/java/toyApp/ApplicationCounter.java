package toyApp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;


@ApplicationScoped
@Named
public class ApplicationCounter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private OddCalculator oddCalculator;

	List<Integer> meanHistory;

	int lastNMean;

	public ApplicationCounter() {
		meanHistory = new ArrayList<>();
		lastNMean = 3;

	}
	
	public void reset() {
		meanHistory = new ArrayList<>();
		lastNMean = 3;
	}

	public List<Integer> getMeanHistory() {
		return meanHistory;
	}

	public void insertMeanListItem(Double item) {
		meanHistory.add(item.intValue());
	}

	public String isLastNMeanInsertedOdd() {

		String response = oddCalculator.calcIfOdd(meanHistory, lastNMean) ? "Odd" : "Even";

		return response;
	}

}
