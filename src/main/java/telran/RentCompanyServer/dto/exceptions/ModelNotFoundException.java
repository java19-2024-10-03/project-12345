package telran.RentCompanyServer.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Model not found")
@ResponseStatus(code = HttpStatus.NOT_FOUND)
@SuppressWarnings("serial")
public class ModelNotFoundException extends RuntimeException {

	public ModelNotFoundException() {

	}

	public ModelNotFoundException(String message) {
		super(message);
	}
}
