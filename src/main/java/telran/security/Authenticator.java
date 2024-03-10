package telran.security;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import telran.accounting.domain.repository.UserAccountsRepository;
import telran.accounting.entity.UserAccount;

@Configuration
public class Authenticator implements UserDetailsService {
	@Autowired
	UserAccountsRepository repo;
	@Value("${activationPeriod: 3}") // in months
	int activationPeriod;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserAccount account = repo.findById(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		String password = account.getHashCode();
		String[] roles = account.getRoles().stream().map(r -> "ROLE_" + r).toArray(String[]::new);

		boolean passIsNotExpired = ChronoUnit.MONTHS.between(account.getActivationDate(),
				LocalDateTime.now()) < activationPeriod;

		return new UserProfile(username, password, AuthorityUtils.createAuthorityList(roles), passIsNotExpired);
	}

}
