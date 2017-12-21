package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class FactoryBean implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2808133337090457758L;

	private int result;
    
    private List<FactoryInfo> factorys;
    
    private List<SubFactory> subfactorys;
    
    private List<DeviceInfo> devices;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<FactoryInfo> getFactorys() {
		return factorys;
	}

	public void setFactorys(List<FactoryInfo> factorys) {
		this.factorys = factorys;
	}

	public List<SubFactory> getSubfactorys() {
		return subfactorys;
	}

	public void setSubfactorys(List<SubFactory> subfactorys) {
		this.subfactorys = subfactorys;
	}

	public List<DeviceInfo> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceInfo> devices) {
		this.devices = devices;
	}
    


    
}
