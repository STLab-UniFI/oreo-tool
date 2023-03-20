package toyApp;

public class SimpleUser {
	
	private Double favouriteMeanNumber;
	
	public SimpleUser() {
		setFavouriteMeanNumber(1.0);
	}

	public SimpleUser(double favouriteMean) {
		setFavouriteMeanNumber(favouriteMean);
	}

	public Double getFavouriteMeanNumber() {
		return favouriteMeanNumber;
	}

	public void setFavouriteMeanNumber(Double favouriteMeanNumber) {
		this.favouriteMeanNumber = favouriteMeanNumber;
	}

}
