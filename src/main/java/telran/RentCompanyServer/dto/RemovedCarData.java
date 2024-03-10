package telran.RentCompanyServer.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RemovedCarData implements Serializable{
private Car car;
private List<RentRecord> removeRecords;

public RemovedCarData() {}

public RemovedCarData(Car car, List<RentRecord> removeRecords) {
	super();
	this.car = car;
	this.removeRecords = removeRecords;
}

public Car getCar() {
	return car;
}

public List<RentRecord> getRemoveRecords(){
	return removeRecords;
}

@Override
public String toString() {
	return "RemovedCarData [car=" + car + ", removeRecords=" + removeRecords + "]";
}

}


