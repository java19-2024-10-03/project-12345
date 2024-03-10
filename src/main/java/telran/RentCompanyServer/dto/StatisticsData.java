package telran.RentCompanyServer.dto;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class StatisticsData implements Serializable {

	LocalDate fromDate;
	LocalDate toDate;
	int fromAge;
	int toAge;
	
	public StatisticsData() {}

	public StatisticsData(LocalDate fromDate, LocalDate toDate, int fromAge, int toAge) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.fromAge = fromAge;
		this.toAge = toAge;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public int getFromAge() {
		return fromAge;
	}

	public void setFromAge(int fromAge) {
		this.fromAge = fromAge;
	}

	public int getToAge() {
		return toAge;
	}

	public void setToAge(int toAge) {
		this.toAge = toAge;
	}
	
	
}
