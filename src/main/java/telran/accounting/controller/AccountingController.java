package telran.accounting.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.accounting.dto.RolesResponseDto;
import telran.accounting.dto.UserAccountResponseDto;
import telran.accounting.dto.UserRegisterDto;
import telran.accounting.dto.UserUpdateDto;
import telran.accounting.service.IAccountingManagement;

@RestController
@RequestMapping("/account")
@EnableMethodSecurity

public class AccountingController {

	@Autowired
	IAccountingManagement service;

	@PostMapping("/register")
	public UserAccountResponseDto registration(@RequestBody UserRegisterDto account) {
		return service.registration(account);
	}

	@PostMapping("/login")
	public UserAccountResponseDto login(Principal principal) {
		return service.getUser(principal.getName());
	}

	@DeleteMapping("/user/{login}")
	public UserAccountResponseDto removeUser(@PathVariable String login) {
		return service.removeUser(login);
	}

	@PutMapping("/user/{login}")
	public UserAccountResponseDto editUser(@PathVariable String login, @RequestBody UserUpdateDto account) {

		return service.editUser(login, account);
	}

	@PutMapping("/password/{login}")
	public boolean updatePassword(Principal principal, @RequestHeader("X-New-Password") String password) {

		return service.updatePassword(principal.getName(), password);
	}

	@PutMapping("/revoke/{login}")
	public boolean revokeAccount(@PathVariable String login) {

		return service.revokeAccount(login);
	}

	@PutMapping("/activate/{login}")
	public boolean activateAccount(@PathVariable String login) {

		return service.activateAccount(login);
	}

	@GetMapping("/activation_date/{login}")
	public LocalDateTime getActivationDate(@PathVariable String login) {
		return service.getActivationDate(login);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/roles/{login}")
	public RolesResponseDto getRoles(@PathVariable String login) {

		return service.getRoles(login);
	}

	@PutMapping("/user/{login}/role/{role}")
	public RolesResponseDto addRole(@PathVariable String login, @PathVariable String role) {

		return service.addRole(login, role);
	}

	@DeleteMapping("/user/{login}/role/{role}")
	public RolesResponseDto removeRole(@PathVariable String login, @PathVariable String role) {

		return service.removeRole(login, role);
	}

}
