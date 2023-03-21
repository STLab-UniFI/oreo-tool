package toyApp;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named
public class MeanCalculator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ApplicationCounter counter;
	
	public double CalcMean(double firstOperator, double secondOperator) {
		double result = (firstOperator + secondOperator) / 2;
		
		counter.insertMeanListItem(result);
		
		return result;
	}
}
