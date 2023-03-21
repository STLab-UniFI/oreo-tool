package toyApp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class OddCalculator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public boolean calcIfOdd(List<Integer> list, int lastN) {
		
		int lastNmeanSummation = 0;
		
		List<Integer> reversedList = new ArrayList<>(list);
		Collections.reverse(reversedList);
		
		for (int i = 0; i < lastN && i< reversedList.size(); i++) {
			lastNmeanSummation += reversedList.get(i);
		}

		return lastNmeanSummation%2 == 1 ;
		
	}
}
