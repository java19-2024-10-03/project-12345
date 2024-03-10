package telran.accounting.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Document(collection="user_accounts")
public class UserAccount {
	@Id
	@Setter(value=AccessLevel.NONE)
	private String login;
	private String hashCode; // crypto password
	private String firstName;
	private String lastName;
	
	private HashSet<String>roles;
	private LocalDateTime activationDate;
	private boolean revoked = false;
	private LinkedList<String> lastHashCodes = new LinkedList<>();
	
	
	public UserAccount(String login, String hashCode, String firstName, String lastName) {
		super();
		this.login = login;
		this.hashCode = hashCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = new HashSet<>();
		this.activationDate = LocalDateTime.now();
		roles.add("USER");
	}
	
	public UserAccount() {
		this.activationDate = LocalDateTime.now();
		this.roles = new HashSet<>();
		roles.add("USER");
	}
}


