package telran.accounting.service;

import java.time.LocalDateTime;

import telran.accounting.dto.RolesResponseDto;
import telran.accounting.dto.UserAccountResponseDto;
import telran.accounting.dto.UserRegisterDto;
import telran.accounting.dto.UserUpdateDto;

public interface IAccountingManagement {
	
	UserAccountResponseDto registration(UserRegisterDto account);
	UserAccountResponseDto removeUser(String login);
	UserAccountResponseDto getUser(String login);
	UserAccountResponseDto editUser(String login, UserUpdateDto account);
	boolean updatePassword(String login, String password);
	boolean revokeAccount(String login);
	boolean activateAccount(String login);
	LocalDateTime getActivationDate(String login);
	RolesResponseDto getRoles(String login);
	RolesResponseDto addRole(String login, String role);
	RolesResponseDto removeRole(String login, String role);
	String getPasswordHash(String login);

}
