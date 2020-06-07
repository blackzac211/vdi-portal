package unist.vdi.vcenter.service;


public class CustomVM {
	private String vm;
	private String name;
	private String powerState;
	private String cpuCount;
	private String memorySize;
	
	private String ipAddress;
	private String description;
	
	
	public String getVm() {
		return vm;
	}
	public void setVm(String vm) {
		this.vm = vm;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPowerState() {
		return powerState;
	}
	public void setPowerState(String powerState) {
		this.powerState = powerState;
	}
	public String getCpuCount() {
		return cpuCount;
	}
	public void setCpuCount(String cpuCount) {
		this.cpuCount = cpuCount;
	}
	public String getMemorySize() {
		return memorySize;
	}
	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
