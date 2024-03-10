package telran.RentCompanyServer.service;

import java.time.temporal.ChronoUnit;

import telran.RentCompanyServer.dto.*;

/*
 * +Default constructor (default values:
finePercent=15,gasPrice=10)
+setters(all fields)
+getters(all fields)
+toString():String
 */
@SuppressWarnings("serial")
public abstract class AbstractRentCompany implements IRentCompany {
protected int finePercent;
protected int gasPrice;


	public AbstractRentCompany() {
	super();
	this.finePercent = 15;
	this.gasPrice = 10;
}

	@Override
	public int getGasPrice() {
		
		return gasPrice;
	}

	@Override
	public void setGasPrice(int price) {
		gasPrice = price;
	}

	@Override
	public int getFinePercent() {
	
		return finePercent;
	}

	@Override
	public void setFinePercent(int finePercent) {
		this.finePercent = finePercent;
	}

	@Override
	public String toString() {
		return "AbstractRentCompany [finePercent=" + finePercent + ", gasPrice=" + gasPrice + "]";
	}

	

}
