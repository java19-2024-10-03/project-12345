package telran.RentCompanyServer.dto;

import java.io.Serializable;
/*
 * -regNumber:String
-licenseId:long
-rentDate:LocalDate
-returnDate:LocalDate
-rentDays:int
-damages:int
-tankPercent:int
-cost:double

+Default constructor
+Constructor using fields(regNumber,licenseId,
rentDate,rentDays)
+getters(all fields)
+toString():String
+setters(damages,tankPercent,cost,returnDate)
+equals/hashCode(all fields)
 */
import java.time.LocalDate;
@SuppressWarnings("serial")
public class RentRecord implements Serializable {
private String regNumber;
private long lisenceId;
private LocalDate rentDate;
private LocalDate returnDate;
private int rentDays;
private int damages;
private int tankPercent;
private double cost;

public RentRecord() {}

public RentRecord(String regNumber, long lisenceId, LocalDate rentDate, int rentDays) {
	super();
	this.regNumber = regNumber;
	this.lisenceId = lisenceId;
	this.rentDate = rentDate;
	this.rentDays = rentDays;
}

public String getRegNumber() {
	return regNumber;
}

public long getLisenceId() {
	return lisenceId;
}

public LocalDate getRentDate() {
	return rentDate;
}

public LocalDate getReturnDate() {
	return returnDate;
}

public int getRentDays() {
	return rentDays;
}

public int getDamages() {
	return damages;
}

public int getTankPercent() {
	return tankPercent;
}

public double getCost() {
	return cost;
}

public void setReturnDate(LocalDate returnDate) {
	this.returnDate = returnDate;
}

public void setDamages(int damages) {
	this.damages = damages;
}

public void setTankPercent(int tankPercent) {
	this.tankPercent = tankPercent;
}

public void setCost(double cost) {
	this.cost = cost;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	long temp;
	temp = Double.doubleToLongBits(cost);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + damages;
	result = prime * result + (int) (lisenceId ^ (lisenceId >>> 32));
	result = prime * result + ((regNumber == null) ? 0 : regNumber.hashCode());
	result = prime * result + ((rentDate == null) ? 0 : rentDate.hashCode());
	result = prime * result + rentDays;
	result = prime * result + ((returnDate == null) ? 0 : returnDate.hashCode());
	result = prime * result + tankPercent;
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	RentRecord other = (RentRecord) obj;
	if (Double.doubleToLongBits(cost) != Double.doubleToLongBits(other.cost))
		return false;
	if (damages != other.damages)
		return false;
	if (lisenceId != other.lisenceId)
		return false;
	if (regNumber == null) {
		if (other.regNumber != null)
			return false;
	} else if (!regNumber.equals(other.regNumber))
		return false;
	if (rentDate == null) {
		if (other.rentDate != null)
			return false;
	} else if (!rentDate.equals(other.rentDate))
		return false;
	if (rentDays != other.rentDays)
		return false;
	if (returnDate == null) {
		if (other.returnDate != null)
			return false;
	} else if (!returnDate.equals(other.returnDate))
		return false;
	if (tankPercent != other.tankPercent)
		return false;
	return true;
}

@Override
public String toString() {
	return "RentRecord [regNumber=" + regNumber + ", lisenceId=" + lisenceId + ", rentDate=" + rentDate
			+ ", returnDate=" + returnDate + ", rentDays=" + rentDays + ", damages=" + damages + ", tankPercent="
			+ tankPercent + ", cost=" + cost + "]";
}


}
