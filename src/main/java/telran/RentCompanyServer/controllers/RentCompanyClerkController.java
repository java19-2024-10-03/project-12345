package telran.RentCompanyServer.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.RentCompanyServer.dto.Car;
import telran.RentCompanyServer.dto.CarsReturnCode;
import telran.RentCompanyServer.dto.Driver;
import telran.RentCompanyServer.dto.Model;
import telran.RentCompanyServer.dto.RemovedCarData;
import telran.RentCompanyServer.dto.RentCarsData;
import telran.RentCompanyServer.dto.ReturnCarData;
import telran.RentCompanyServer.service.IRentCompany;

@RestController
public class RentCompanyClerkController {
	@Autowired
	IRentCompany company;
	
	@PostMapping("/driver/add")
	CarsReturnCode addDriver(@RequestBody Driver driver) {
		return company.addDriver(driver);
	}
	
	@GetMapping("/model")
	Model getModel(@RequestParam("modelName") String modelName) {
		return company.getModel(modelName);
	}
	
	@GetMapping("/driver/{licenseId}/cars")
	List<Car> getDriverCars(@PathVariable long licenseId){
		return company.getDriverCars(licenseId);
	}
	
	@GetMapping("/model/{modelName}/cars")
	List<Car> getModelCars(@PathVariable String modelName){
		return company.getModelCars(modelName);
	}
	
	@GetMapping("/car/{regNumber}/drivers")
	List<Driver> getCarDrivers(@PathVariable String regNumber){
		return company.getCarDrivers(regNumber);
	}
	
	@GetMapping("/driver/{licenseId}")
	Driver getDriver(@PathVariable long licenseId) {
		return company.getDriver(licenseId);
	}
	
	@GetMapping("/car/{regNumber}")
	Car getCar(@PathVariable String regNumber) {
		return company.getCar(regNumber);
	}
	
	@GetMapping("/models")
	List<String> getModelNames(){
		return company.getModelNames();
	}
	
	@PostMapping("/car/return")
	RemovedCarData returnCar(@RequestBody ReturnCarData returnCar) {
		return company.returnCar(returnCar);
	}
	
	@PostMapping("/car/rent")
	CarsReturnCode rentCar(@RequestBody RentCarsData rentCar) {
		return company.rentCar(rentCar);
	}
	
	
	
	
}
