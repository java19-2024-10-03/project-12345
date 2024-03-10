package telran.accounting.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import telran.accounting.domain.repository.UserAccountsRepository;
import telran.accounting.dto.RolesResponseDto;
import telran.accounting.dto.UserAccountResponseDto;
import telran.accounting.dto.UserRegisterDto;
import telran.accounting.dto.UserUpdateDto;
import telran.accounting.entity.UserAccount;

@Service
public class AccountingMongo implements IAccountingManagement, CommandLineRunner {
	
	@Value("${n_last_hashcodes:3}")
	private int n_last_hashcodes;
	@Value("${password_length:8}")
	private int passwordLength;
	@Autowired
	UserAccountsRepository repo;
	@Autowired
	PasswordEncoder encoder;

	@Override
	public UserAccountResponseDto registration(UserRegisterDto account) {
		
		if(repo.existsById(account.getLogin()))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "User with same login already exists");
		
		if(!isPasswordValid(account.getPassword()))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Password not valide");
		
		UserAccount accountMongo = new UserAccount(account.getLogin(), 
				getHashCode(account.getPassword()), account.getFirstName(), account.getLastName());
		
		repo.save(accountMongo);
		
		return new UserAccountResponseDto(account.getLogin(), account.getFirstName(),
				account.getLastName(), accountMongo.getRoles());
	}

	private String getHashCode(String password) {
		return encoder.encode(password);
	}

	private boolean isPasswordValid(String password) {
		return password.length() > passwordLength;
	}
//---------------------------------
	@Override
	public UserAccountResponseDto removeUser(String login) {
		UserAccount user = repo.findById(login).orElse(null);
		if( user == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found");
		repo.deleteById(login);
		return new UserAccountResponseDto(user.getLogin(), user.getFirstName(), user.getLastName(), user.getRoles());
	}

	@Override
	public UserAccountResponseDto getUser(String login) {
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		return new UserAccountResponseDto(user.getLogin(), user.getFirstName(), user.getLastName(), user.getRoles());
	}

	@Override
	public UserAccountResponseDto editUser(String login, UserUpdateDto account) {
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		
		if(account != null && account.getFirstName()!=null && account.getLastName()!=null) {	
		user.setFirstName(account.getFirstName());
		user.setLastName(account.getLastName());
		repo.save(user);
		}
		return new UserAccountResponseDto(user.getLogin(), user.getFirstName(), user.getLastName(), user.getRoles());
	}

	@Override
	public boolean updatePassword(String login, String password) {
		if(!isPasswordValid(password))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Password not valide");
		UserAccount account = repo.findById(login).orElseThrow(() -> 
		new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		
		if(encoder.matches(password, account.getHashCode()))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Yuo need new password");
		LinkedList<String>lastHashCodes = account.getLastHashCodes();
		
		if(lastHashCodes.size() == n_last_hashcodes)
			lastHashCodes.removeFirst();
		
		lastHashCodes.add(account.getHashCode());
		
		account.setHashCode(encoder.encode(password));
		account.setActivationDate(LocalDateTime.now());
		repo.save(account);
		return true;
	}

	@Override
	public boolean revokeAccount(String login) {
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		if(user.isRevoked())
			throw new ResponseStatusException(HttpStatus.CONFLICT, "This account already revoked");
		
		user.setRevoked(true);
		repo.save(user);
		return true;
	}

	@Override
	public boolean activateAccount(String login) {
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		if(!user.isRevoked())
			throw new ResponseStatusException(HttpStatus.CONFLICT, "This account already activated");
		user.setRevoked(false);
		user.setActivationDate(LocalDateTime.now());
		repo.save(user);
		return true;
	}

	@Override
	public LocalDateTime getActivationDate(String login) {
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		
		return user.isRevoked() ? null : user.getActivationDate();
	}

	@Override
	public RolesResponseDto getRoles(String login) {
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		
		return user.isRevoked() ? null : new RolesResponseDto(login, user.getRoles());
	}

	@Override
	public RolesResponseDto addRole(String login, String role) {
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		HashSet<String> roles = user.getRoles();
		if (roles.contains(role))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "User with  login " + login +" alredy has role " + role);
		roles.add(role);
		repo.save(user);
		return new RolesResponseDto(login, user.getRoles());

	}

	@Override
	public RolesResponseDto removeRole(String login, String role) {
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		HashSet<String> roles = user.getRoles();
		if (!roles.contains(role))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "User with  login " + login +" hasn't role " + role);
		roles.remove(role);
		repo.save(user);
		return new RolesResponseDto(login, user.getRoles());
	}
	
	@Override
	public String getPasswordHash(String login)
	{
		UserAccount user = repo.findById(login).orElseThrow(() 
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with  login " + login +" not found"));
		return user.isRevoked() ? null  : user.getHashCode();
	}

	@Override
	public void run(String... args) throws Exception {
		if(!repo.existsById("admin")) {
			UserAccount admin = new UserAccount("admin", encoder.encode("admin"), "", "");
			admin.setRoles(new HashSet<>(Arrays.asList("ADMIN")));
			repo.save(admin);
		}
		
	}


}
