package telran.RentCompanyServer.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import telran.RentCompanyServer.dto.Driver;
import telran.RentCompanyServer.dto.RentRecord;
import telran.RentCompanyServer.service.IRentCompany;

@RestController
public class RentCompanyStatisticController {
	@Autowired
	IRentCompany company;
	
	@DateTimeFormat(iso=ISO.DATE)
	@GetMapping("/models/popular/{dateFrom}/{dateTo}/{ageFrom}/{ageTo}")
	List<String> getMostPopularCarModels(@PathVariable("dateFrom") String dataFrom,
			@PathVariable("dateTo") String dataTo, 
			@PathVariable int ageFrom, @PathVariable int ageTo)
	{
		LocalDate from = LocalDate.parse(dataFrom);
		LocalDate to  = LocalDate.parse(dataTo);
		
		return company.getMostPopularCarModels(from, to, ageFrom, ageTo);
	}
	
	@GetMapping("/drivers/active")
	List<Driver> getMostActiveDrivers(){
		return company.getMostActiveDrivers();
	}
	
	@GetMapping("/models/profitable/{dateFrom}/{dateTo}")
	List<String> getMostProfitableCarModels (@PathVariable("dateFrom") String dataFrom,
			@PathVariable("dateTo") String dataTo) {
		
		LocalDate from = LocalDate.parse(dataFrom);
		LocalDate to  = LocalDate.parse(dataTo);
		
		return company.getMostProfitableCarModels(from, to);	
	}
	
	@GetMapping("/records/{dataFrom}/{dataTo}")
	List<RentRecord> getRentRecordsAtDates(@PathVariable("dateFrom") String dataFrom,
			@PathVariable("dateTo") String dataTo) {
		
		LocalDate from = LocalDate.parse(dataFrom);
		LocalDate to  = LocalDate.parse(dataTo);
		
		return company.getRentRecordsAtDates(from, to);
	}
}
