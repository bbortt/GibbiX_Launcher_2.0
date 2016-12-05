package ch.gibb.iet.modul306.vmlauncher.model;

import java.util.List;

@Deprecated
public class vmModel {
	private long id;
	private String name;
	private String network;
	private String os;
	private String type;
	private double ram;
	private double diskSpace;
	private String busSystem;
	private String ipAdress;
	private String macAdress;
	private List<String> softwares;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getRam() {
		return ram;
	}

	public void setRam(double ram) {
		this.ram = ram;
	}

	public double getDiskSpace() {
		return diskSpace;
	}

	public void setDiskSpace(double diskSpace) {
		this.diskSpace = diskSpace;
	}

	public String getBusSystem() {
		return busSystem;
	}

	public void setBusSystem(String busSystem) {
		this.busSystem = busSystem;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public String getMacAdress() {
		return macAdress;
	}

	public void setMacAdress(String macAdress) {
		this.macAdress = macAdress;
	}

	public List<String> getSoftwares() {
		return softwares;
	}

	public void setSoftwares(List<String> softwares) {
		this.softwares = softwares;
	}
}