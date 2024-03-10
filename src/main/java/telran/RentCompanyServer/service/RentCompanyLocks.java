package telran.RentCompanyServer.service;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RentCompanyLocks {
	static final ReadWriteLock carsLock = new ReentrantReadWriteLock();
	static final ReadWriteLock driversLock = new ReentrantReadWriteLock();
	static final ReadWriteLock modelsLock = new ReentrantReadWriteLock();
	static final ReadWriteLock recordsLock = new ReentrantReadWriteLock();
	
	static final int CARS_INDEX = 0;
	static final int DRIVERS_INDEX = 1;
	static final int MODELS_INDEX = 2;
	static final int RECORDS_INDEX = 3;
	static final int WRITE = 0;
	static final int READ = 1;
	
	static Lock[][] locks = new Lock[2][4];
	
	static {
		ReadWriteLock[] rwl = {carsLock, driversLock, modelsLock, recordsLock};
		
		for(int i = 0; i < rwl.length; i++) {
			locks[WRITE][i] = rwl[i].writeLock();
			locks[READ][i] = rwl[i].readLock();
		}
	}
	private static void lockUnlock(boolean flock, int typeLock, int ...indexes) {
		Arrays.sort(indexes);
		if(flock)
		lock(typeLock, indexes);
		else
			unlock(typeLock, indexes);
	}
	private static void unlock(int typeLock, int[] indexes) {
		for(int index : indexes) {
			locks[typeLock][index].unlock();
		}
		
	}
	private static void lock(int typeLock, int[] indexes) {
		for(int index : indexes) {
			locks[typeLock][index].lock();		
	}
}
	public static void lockUnlock_addModel(boolean flock) {
		lockUnlock(flock, WRITE, MODELS_INDEX);
	}
	
	public static void lockUnlock_addCar(boolean flock) {
		lockUnlock(flock, READ, MODELS_INDEX);
		lockUnlock(flock, WRITE, CARS_INDEX);
	}
	
	public static void lockUnlock_rentCar(boolean flock) {
		lockUnlock(flock, READ, DRIVERS_INDEX);
		lockUnlock(flock, WRITE, CARS_INDEX, RECORDS_INDEX);
	}
	
	public static void lockUnlock_save(boolean flock) {
		lockUnlock(flock, READ,  0,1,2,3);
	}
	
	
	
}
