package telran.RentCompanyServer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import telran.RentCompanyServer.dto.Car;
import telran.RentCompanyServer.dto.CarsReturnCode;
import telran.RentCompanyServer.dto.Model;
import telran.RentCompanyServer.dto.RemovedCarData;
import telran.RentCompanyServer.service.IRentCompany;
import telran.RentCompanyServer.utils.Persistable;

@RestController
public class RentCompanyAdministratorController {
	@Value("${fileName:company.data}")
	private String fileName;
	@Value("${finePercent:15}")
	private int finePercent;
	@Value(("${gasPrice:10}"))
	private int price;
	
	
	@Autowired
	IRentCompany company;
	
	@ResponseStatus(code=HttpStatus.CREATED, reason="Car was successfully created")
	@PostMapping(value="/car/add")
	CarsReturnCode addCar(@RequestBody Car car) {
		return company.addCar(car);
	}
	
	@PostMapping("/model/add")
	CarsReturnCode addModel(@RequestBody Model model) {
		return company.addModel(model);
	}
	
	@DeleteMapping("/model/remove/{id}")
	List<RemovedCarData> removeModel(@PathVariable String id){
		return company.removeModel(id);
	}
	
	@DeleteMapping("/car/remove/{regNumber}")
	RemovedCarData removeCar(@PathVariable String regNumber) {
		return company.removeCar(regNumber);
	}
	
	@PreDestroy
	public void saveCompanyToFile() {
		if(company instanceof Persistable) {
			((Persistable) company).save(fileName);
		}
	}
	
	@PostConstruct
	public void settings() {
		company.setFinePercent(finePercent);
		company.setGasPrice(price);
	}
}
