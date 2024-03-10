package telran.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@SuppressWarnings("serial")
public class UserProfile extends User {

	private boolean passIsNotExpired;
	private boolean revoked;

	public UserProfile(String username, String password, Collection<? extends GrantedAuthority> authorities,
			boolean passIsNotExpired) {
		super(username, password, authorities);
		this.passIsNotExpired = passIsNotExpired;
	}

	public boolean getPassIsNotExpired() {
		return passIsNotExpired;
	}

	public boolean getRevoked() {
		return revoked;
	}
}
