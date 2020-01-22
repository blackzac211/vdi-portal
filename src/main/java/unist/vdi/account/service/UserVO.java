package unist.vdi.account.service;

public class UserVO {
	private String id;
	private String name;
	private String erpid;
	private String ip;
	private String privilege;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getErpid() {
		return erpid;
	}
	public void setErpid(String erpid) {
		this.erpid = erpid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
}
