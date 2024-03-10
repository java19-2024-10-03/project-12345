package telran.RentCompanyServer.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static telran.RentCompanyServer.service.RentCompanyLocks.*;
import telran.RentCompanyServer.dto.*;
import telran.RentCompanyServer.dto.exceptions.ModelNotFoundException;
import telran.RentCompanyServer.utils.Persistable;

@SuppressWarnings("serial")
public class RentCompanyEmbedded extends AbstractRentCompany implements Persistable {
	
private static final int REMOVE_THRESHOLD = 60;
private static final int BAD_THRESHOLD = 30;
private static final int GOOD_THRESHOLD = 10;
private HashMap<String, Car> cars = new HashMap<>();
private HashMap<Long, Driver> drivers = new HashMap<>();
private HashMap<String,Model> models = new HashMap<>();
//=========Sprint2============================
private HashMap<String, List<RentRecord>> carRecords = new HashMap<>();
private HashMap<Long, List<RentRecord>> driverRecords = new HashMap<>();
private HashMap<String, List<Car>> modelCars = new HashMap<>();
private TreeMap<LocalDate, List<RentRecord>> records = new TreeMap<>();

	@Override
	public CarsReturnCode addModel(Model model) {
		RentCompanyLocks.lockUnlock_addModel(true);
		try {
			return models.putIfAbsent(model.getModelName(), model) == null ? CarsReturnCode.OK
					: CarsReturnCode.MODEL_EXISTS;
		} finally {
			lockUnlock_addModel(false);
		}
	}

	@Override
	public CarsReturnCode addCar(Car car) {
		lockUnlock_addCar(true);
		try {
			if (!models.containsKey(car.getModelName()))
//				return CarsReturnCode.NO_MODEL;
				throw new ModelNotFoundException("Model"+car.getModelName()+"not found");
			//return cars.putIfAbsent(car.getRegNumber(), car) == null? CarsReturnCode.OK : CarsReturnCode.CAR_EXISTS;
			if (cars.putIfAbsent(car.getRegNumber(), car) == null) {
				//			List<Car> temp =  modelCars.getOrDefault(car.getModelName(), new ArrayList<>());
				//			temp.add(car);
				//			modelCars.put(car.getModelName(), temp);
				putListElemToMap(modelCars, car.getModelName(), car);
				return CarsReturnCode.OK;
			} else
//				return CarsReturnCode.CAR_EXISTS;
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Car already exists");
		} finally {
			lockUnlock_addCar(false);
		}
	}

	@Override
	public CarsReturnCode addDriver(Driver driver) {	
		return drivers.putIfAbsent(driver.getLicenseId(), driver) == null? CarsReturnCode.OK : CarsReturnCode.DRIVER_EXISTS;
	}

	@Override
	public Model getModel(String modelName) {
		
		return models.get(modelName);
	}

	@Override
	public Car getCar(String regNumber) {
		
		return cars.get(regNumber);
	}

	@Override
	public Driver getDriver(long licenseId) {
		
		return drivers.get(licenseId);
	}

	@Override
	public void save(String fileName) {
		
		lockUnlock_save(true);
			try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {

				out.writeObject(this);
				System.out.println("File saved");

			} catch (IOException e) {
				System.out.println("Error in method SAVE" + e.getMessage());
			 
		} finally {
		lockUnlock_save(false);
		}

	}
	//+static restoreFromFile(fileName:String):IRentCompany
public static IRentCompany restoreFromFile(String fileName) {
	try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))){
		return (IRentCompany) in.readObject();
	} catch (Exception e) {
		System.out.println("new object was created" + e.getMessage());
		return new RentCompanyEmbedded();
	} 
}
//========================Sprint2====================================

@Override
public CarsReturnCode rentCar(RentCarsData rentCar) {
	lockUnlock_rentCar(true);
	try {
		if (!cars.containsKey(rentCar.getRegNumber()))
			return CarsReturnCode.NO_CAR;
		if (!drivers.containsKey(rentCar.getLicenseId()))
			return CarsReturnCode.NO_DRIVER;
		if (cars.get(rentCar.getRegNumber()).isFlRemoved())
			return CarsReturnCode.CAR_REMOVED;
		if (cars.get(rentCar.getRegNumber()).isInUse())
			return CarsReturnCode.CAR_IN_USE;
		RentRecord rec = new RentRecord(rentCar.getRegNumber(), rentCar.getLicenseId(), rentCar.getRentDate(), rentCar.getRentDays());
		putListElemToMap(carRecords, rentCar.getRegNumber(), rec);
		//	System.out.println(carRecords.values());
		putListElemToMap(driverRecords, rentCar.getLicenseId(), rec);
		//	System.out.println(driverRecords.values());
		putListElemToMap(records, rentCar.getRentDate(), rec);
		//	System.out.println(records.values());
		return CarsReturnCode.OK;
	} finally {
	lockUnlock_rentCar(false);
	}
}

//	@Override
//	public CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays) {
//		lockUnlock_rentCar(true);
//		try {
//			if (!cars.containsKey(regNumber))
//				return CarsReturnCode.NO_CAR;
//			if (!drivers.containsKey(licenseId))
//				return CarsReturnCode.NO_DRIVER;
//			if (cars.get(regNumber).isFlRemoved())
//				return CarsReturnCode.CAR_REMOVED;
//			if (cars.get(regNumber).isInUse())
//				return CarsReturnCode.CAR_IN_USE;
//			RentRecord rec = new RentRecord(regNumber, licenseId, rentDate, rentDays);
//			putListElemToMap(carRecords, regNumber, rec);
//			//	System.out.println(carRecords.values());
//			putListElemToMap(driverRecords, licenseId, rec);
//			//	System.out.println(driverRecords.values());
//			putListElemToMap(records, rentDate, rec);
//			//	System.out.println(records.values());
//			return CarsReturnCode.OK;
//		} finally {
//		lockUnlock_rentCar(false);
//		}
//	}
private static <K, T> void putListElemToMap(Map<K, List<T>> map, K key, T elem) {
	List<T> temp = map.getOrDefault(key, new ArrayList<>());
	temp.add(elem);
	map.put(key, temp);
}
	@Override
	public List<Car> getDriverCars(long licenseId) {
		
		List<RentRecord>temp = driverRecords.get(licenseId);
		if(temp!=null)
		return temp.stream().map(rec -> cars.get(rec.getRegNumber())).
				collect(Collectors.toList());
		else return new ArrayList<Car>();
	}

	@Override
	public List<Driver> getCarDrivers(String regNumber) {
		List<RentRecord> temp = carRecords.get(regNumber);
		if(temp!=null)
		return temp.stream().map(rec -> drivers.get(rec.getLisenceId())).collect(Collectors.toList());
		else return new ArrayList<Driver>();
	}

	@Override
	public List<Car> getModelCars(String modelName) {
		
	//	return cars.values().stream().filter(car -> car.getModelName().equals(modelName)).collect(Collectors.toList());
		return modelCars.get(modelName);
	}

	@Override
	public List<RentRecord> getRentRecordsAtDates(LocalDate from, LocalDate to) {
		
		return records.values().stream().flatMap(list -> list.stream()).filter(rec -> rec.getRentDate().isAfter(from)&&
				rec.getRentDate().isBefore(to)).collect(Collectors.toList());
	}
//======================Sprint 3======================================
	@Override
	public RemovedCarData removeCar(String regNumber) {
		Car car = getCar(regNumber);
		if(car == null || car.isFlRemoved())
		return null;
		car.setFlRemoved(true);
		return car.isInUse()? new RemovedCarData(car, null) : actualCarRemove(car);
				}

	private RemovedCarData actualCarRemove(Car car) {
		String regNumber = car.getRegNumber();
	List<RentRecord> removedRecords = carRecords.getOrDefault(regNumber, new ArrayList<>());
	cars.remove(regNumber);
	carRecords.remove(regNumber);
	removeFromDriverRecords(removedRecords);
	removeFromRecords(removedRecords);
	modelCars.get(car.getModelName()).remove(car);
	return new RemovedCarData(car, removedRecords);
}



	private void removeFromRecords(List<RentRecord> removedRecords) {
		for (RentRecord rentRecord : removedRecords) {
			records.get(rentRecord.getRentDate()).remove(rentRecord);
		}
		
	}

	private void removeFromDriverRecords(List<RentRecord> removedRecords) {
		for (RentRecord rentRecord : removedRecords) {
			driverRecords.get(rentRecord.getLisenceId()).remove(rentRecord);
		}
		
	}

	@Override
	public List<RemovedCarData> removeModel(String modelName) {
		List<Car> carsModel = modelCars.getOrDefault(modelName, new ArrayList<>());
		return carsModel.stream().filter(c -> !c.isFlRemoved()).map(c -> removeCar(c.getRegNumber())).collect(Collectors.toList());
	}

	@Override
	public RemovedCarData returnCar(ReturnCarData returnCar) {
		//==========find record======================
		RentRecord rec = getRentRecord(returnCar.getRegNumber(), returnCar.getLicenseId());
		if(rec == null)
		return null;
		updateRecord(rec, returnCar.getReturnDate(), returnCar.getDamages(), returnCar.getTankPercent());
		Car car = getCar(returnCar.getRegNumber());
		updateCar(car, returnCar.getDamages());
		return car.isFlRemoved() || returnCar.getDamages() > REMOVE_THRESHOLD ? actualCarRemove(car) : new RemovedCarData(car, null);
	}
	
//	@Override
//	public RemovedCarData returnCar(String regNumber, long licensId, LocalDate returnDate, int damages,
//			int tankPercent) {
//		//==========find record======================
//		RentRecord rec = getRentRecord(regNumber, licensId);
//		if(rec == null)
//		return null;
//		updateRecord(rec, returnDate, damages, tankPercent);
//		Car car = getCar(regNumber);
//		updateCar(car, damages);
//		return car.isFlRemoved() || damages > REMOVE_THRESHOLD ? actualCarRemove(car) : new RemovedCarData(car, null);
//	}

	private void updateCar(Car car, int damages) {
		car.setInUse(false);
		if(damages >= BAD_THRESHOLD)
			car.setState(State.BAD);
		else if (damages >= GOOD_THRESHOLD)
			car.setState(State.GOOD);
		
	}

	private RentRecord getRentRecord(String regNumber, long licensId) {
		
		return driverRecords.get(licensId).stream().filter(r -> r.getRegNumber().equals(regNumber)&&r.getReturnDate()==null)
				.findFirst().orElse(null);
	}

	private void updateRecord(RentRecord rec, LocalDate returnDate, int damages, int tankPercent) {
		rec.setReturnDate(returnDate);
		rec.setDamages(damages);
		rec.setTankPercent(tankPercent);
		 //========compute cost==========================
		double cost = computeCost(getRentPrice(rec.getRegNumber()), rec.getRentDays(),
				getDelay(rec), tankPercent, getTankVolume(rec.getRegNumber()));
		rec.setCost(cost);
	
	}

	protected double computeCost(int rentPrice, int rentDays, int delay, int tankPercent, int tankVolume) {
		double cost = rentDays * rentPrice;
		if(delay > 0)
			cost += delayFee(delay, rentPrice);
		if(tankPercent<100)
			cost += additionalGasCost(tankPercent, tankVolume);
		return cost;
	}

	private double additionalGasCost(int tankPercent, int tankVolume) {
		
		return gasPrice*(100-tankPercent)/100. * tankVolume;
	}

	private double delayFee(int delay, int rentPrice) {
		
		return delay * (rentPrice + finePercent/100.);
	}

	private int getTankVolume(String regNumber) {
		String modelName = cars.get(regNumber).getModelName();
		return models.get(modelName).getGasTank();
	}

	private int getDelay(RentRecord rec) {
		long realRentDays = ChronoUnit.DAYS.between(rec.getRentDate(), rec.getReturnDate());
		int delta = (int) (realRentDays - rec.getRentDays());
		return delta<=0? 0 : delta;
	}

	private int getRentPrice(String regNumber) {
		String modelName = cars.get(regNumber).getModelName();
		return models.get(modelName).getPriceDay();
	}

	@Override
	public List<String> getMostPopularCarModels(LocalDate dataFrom, LocalDate dataTo, int ageFrom, int ageTo) {
		if(ageTo<ageFrom || dataTo.isBefore(dataFrom))
		return null;
		List<RentRecord>recInPeriod = getRentRecordsAtDates(dataFrom, dataTo);

		Map<String, Long> mapOccurrences = recInPeriod.stream().filter(r -> isInAgeInterval(r, ageFrom, ageTo))
				.collect( Collectors.groupingBy(r -> getCar(r.getRegNumber()).getModelName(), Collectors.counting()));
		
		long max = Collections.max(mapOccurrences.values());
		List<String> res = new ArrayList<>();
		mapOccurrences.forEach((k,v) -> {
			if(v == max)
				res.add(k);
		});
		return res;
	}

	private boolean  isInAgeInterval(RentRecord r, int ageFrom, int ageTo) {
		LocalDate rentDate = r.getRentDate();
		Driver driver = drivers.get(r.getLisenceId());
		int driversAge = rentDate.getYear() - driver.getBirthYear();
		return driversAge>=ageFrom&&driversAge<ageTo;
	}

	@Override
	public List<String> getMostProfitableCarModels(LocalDate dataFrom, LocalDate dataTo) {
		// another solution
		Collection<List<RentRecord>> recInDateInterval = records.subMap(dataFrom, dataTo).values();
		if(recInDateInterval == null)
			return new ArrayList<>();
		Map<String, Double> modelProfit = recInDateInterval.stream().flatMap(List::stream)
				.collect(Collectors.groupingBy(r -> getCar(r.getRegNumber()).getModelName(),
						Collectors.summingDouble(r -> r.getCost())));
		
		if(modelProfit.isEmpty())
			return new ArrayList<>();
		double max = getMaxProfit(modelProfit);
		List<String> res = new ArrayList<>();
		modelProfit.forEach((k, v) -> {
			if(v == max)
				res.add(k);
		});
		return res;
	}
	
private double getMaxProfit(Map<String, Double> modelProfit) {
		
		return modelProfit.values().stream().mapToDouble(x -> x).max().getAsDouble();
	}

	@Override
	public List<Driver> getMostActiveDrivers() {
		long max = 0, maxFinal;
		for (List<RentRecord> val :  driverRecords.values()) {
			max = val.size()>max? val.size() : max;
		}
		maxFinal =max;
		List<Driver> res = new ArrayList<>();
		driverRecords.forEach((k, v) -> {
			if(v.size() == maxFinal)
				res.add(getDriver(k));
		});
		return res;
	}
//======================Sprint5======================================
	@Override
	public List<String> getModelNames() {
		
		return new ArrayList<>(models.keySet());
	}
}
