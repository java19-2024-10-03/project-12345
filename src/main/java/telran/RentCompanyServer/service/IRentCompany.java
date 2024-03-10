package telran.RentCompanyServer.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import telran.RentCompanyServer.dto.*;

public interface IRentCompany extends Serializable {
	
	int getGasPrice(); //price for one liter of a company
	void setGasPrice(int price);  //setting price of one liter
	int getFinePercent();   //fine of one delay day
	void setFinePercent(int finePercent);    //setting fine
	CarsReturnCode addModel(Model model);
	CarsReturnCode addCar(Car car);
	CarsReturnCode addDriver(Driver driver);
	Model getModel(String modelName);
	Car getCar(String regNumber);
	Driver getDriver(long licenseId);
//============Sprint2============================

	CarsReturnCode rentCar(RentCarsData rentCar);
//	CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays);
	List<Car> getDriverCars(long licenseId);
	List<Driver> getCarDrivers(String regNumber);
	List<Car> getModelCars(String modelName);
	List<RentRecord> getRentRecordsAtDates(LocalDate from, LocalDate to);

	 //===================Sprint3====================

	RemovedCarData removeCar(String regNumber);
	List<RemovedCarData> removeModel(String modelName);
//	RemovedCarData  returnCar(String regNumber, long licensId, LocalDate returnDate, int damages, int tankPercent);
	RemovedCarData  returnCar(ReturnCarData returnCar);
	
	/*
	 //Sprint4
+getMostPopularCarModels(dataFrom:LocalDate,dataTo:LocalDate,ageFrom:int,ageTo:int):List<String>
+getMostProfitableCarModels(dataFrom:LocalDate,dataTo:LocalDate):List<String>
+getMostActiveDrivers():List<Driver>
	 */
	
	List<String> getMostPopularCarModels(LocalDate dataFrom, LocalDate dataTo, int ageFrom, int ageTo);
	List<String>getMostProfitableCarModels(LocalDate dataFrom, LocalDate dataTo);
	List<Driver>getMostActiveDrivers();
	//================Sprint5===========================
	List<String>getModelNames();
}
