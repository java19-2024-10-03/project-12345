package telran.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserAccountResponseDto {
	private String login;
	 private String firstName;
	 private String lastName;
	 private Set<String> roles;
}
