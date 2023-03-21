package toyApp;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named
public class LoggedSessionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private NavigatorController navigator;
	
//	@Inject
//	private ApplicationCounter counter; // XXX implementare il fav number = lastMean
	
	private SimpleUser user;
	
	public String login() {
		setUser(new SimpleUser(2.0));
		return navigator.navigate("app-simple-home");
	}
	
	public String logout() {
		setUser(null);
		return navigator.navigate("app-simple-home");
	}

	public boolean isLoggedIn() {
		return user != null;
	}
	
	public boolean isLoggedInTest() {
		return user != null;
	}

	public SimpleUser getUser() {
		return user;
	}

	public void setUser(SimpleUser user) {
		this.user = user;
	}

}
