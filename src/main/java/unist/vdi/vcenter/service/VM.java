package unist.vdi.vcenter.service;

public class VM {
	private String vm;
	private String name;
	private String powerState;
	private String cpuCount;
	private String memorySize;
	
	
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
}
