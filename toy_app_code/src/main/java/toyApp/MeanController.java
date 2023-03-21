package toyApp;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ConversationScoped
@Named
public class MeanController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private MeanCalculator calculator;
	
	@Inject
	private LoggedSessionBean loggedBean;

	@Inject
	private NavigatorController navigator;

	private double firstOperator;

	private double secondOperator;

	private double result;

	private boolean useFavourite;
	
	public MeanController() {
		this.useFavourite = false;
	}


	public String confirm() {

		double choosenOperator;
		if (isUseFavourite() && loggedBean.isLoggedIn()) {
			choosenOperator = loggedBean.getUser().getFavouriteMeanNumber();
		}
		else
			choosenOperator = secondOperator;

		setResult(calculator.CalcMean(firstOperator, choosenOperator));

		return navigator.navigate("app-simple-mean-confirm"); 
	}
	
	public String close() {
		return navigator.navigateAndCloseConv("app-simple-home");
	}

	public boolean isUseFavourite() {
		return useFavourite;
	}

	public void setUseFavourite(boolean useFavourite) {
		this.useFavourite = useFavourite;
	}

	public double getFirstOperator() {
		return firstOperator;
	}

	public void setFirstOperator(double firstOperator) {
		this.firstOperator = firstOperator;
	}

	public double getSecondOperator() {
		return secondOperator;
	}

	public void setSecondOperator(double secondOperator) {
		this.secondOperator = secondOperator;
	}


	public double getResult() {
		return result;
	}


	public void setResult(double result) {
		this.result = result;
	}
}
