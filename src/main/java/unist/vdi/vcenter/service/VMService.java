package unist.vdi.vcenter.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vmware.vcenter.FolderTypes;
import com.vmware.vcenter.VM;
import com.vmware.vcenter.VMTypes;
import com.vmware.vcenter.VMTypes.FilterSpec.Builder;
import com.vmware.vcenter.vm.guest.Identity;

import unist.vdi.common.DBManager;
import vmware.samples.vcenter.helpers.ClusterHelper;
import vmware.samples.vcenter.helpers.DatacenterHelper;

public class VMService {
	
	public String getVMName(String vmId, VDIConnection conn) {
		VM vm = conn.getVapiAuthHelper().getStubFactory().createStub(VM.class, conn.getSessionStubConfig());
		return vm.get(vmId).getName();
	}
	
	public List<CustomVM> getVMList(String userId, VDIConnection conn) {
		try {
			String datacenterName = "";
			String clusterName = "";
			
			VM vm = conn.getVapiAuthHelper().getStubFactory().createStub(VM.class, conn.getSessionStubConfig());
			Identity identity = conn.getVapiAuthHelper().getStubFactory().createStub(Identity.class, conn.getSessionStubConfig());

			Builder bldr = new Builder();
			if (null != datacenterName && !datacenterName.isEmpty()) {
				bldr.setDatacenters(Collections.singleton(DatacenterHelper.getDatacenter(
						conn.getVapiAuthHelper().getStubFactory(), conn.getSessionStubConfig(), datacenterName)));
			}
			if (null != clusterName && !clusterName.isEmpty()) {
				bldr.setClusters(Collections.singleton(ClusterHelper.getCluster(
						conn.getVapiAuthHelper().getStubFactory(), conn.getSessionStubConfig(), clusterName)));
			}
			
			// VM Folder Filter
			Set<String> set = new HashSet<String>();
			List<FolderTypes.Summary> folderList = FolderService.getAllFolders(conn.getVapiAuthHelper().getStubFactory(), conn.getSessionStubConfig());
			for(int i = 0; i < folderList.size(); i++) {
				if(!folderList.get(i).getName().equals("new")) {
					set.add(folderList.get(i).getFolder());
				}
			}
			bldr.setFolders(set);
				
			List<VMTypes.Summary> list = vm.list(bldr.build());
			List<CustomVM> resList = new ArrayList<CustomVM>();

			for (VMTypes.Summary vmSummary : list) {
				if (vmSummary.getName().startsWith(userId + "-")) {
					CustomVM temp = new CustomVM();
					temp.setVm(vmSummary.getVm());
					temp.setName(vmSummary.getName());
					temp.setPowerState(vmSummary.getPowerState().toString());
					try {
						temp.setIpAddress(identity.get(vmSummary.getVm()).getIpAddress());
					} catch (Exception e) {
						temp.setIpAddress("");
					}
					resList.add(temp);
				}
			}
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public HashMap<String, String> getVMListByDB(String userId) {
		DBManager db = new DBManager();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT name, description FROM vm WHERE name LIKE '"+userId+"-%'";
			db.getPreparedStatement(sql);
			pstmt = db.getPreparedStatement(sql);
			rs = pstmt.executeQuery();
			
			HashMap<String, String> map = new HashMap<String, String>();
			while(rs.next()) {
				map.put(rs.getString("name"), rs.getString("description"));
			}
			return map;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try { rs.close(); } catch(Exception e) {}
			try { pstmt.close(); } catch(Exception e) {}
			db.close();
		}
	}
	
	public void updateDescription(CustomVM vm) {
		DBManager db = new DBManager();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			String sql = "SELECT count(*) FROM vm WHERE name=?";
			pstmt = db.getPreparedStatement(sql);
			pstmt.setString(1, vm.getName());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
			pstmt.close();
			if(count > 0) {
				sql = "UPDATE vm SET description = ?, upd_date = SYSDATE(), upd_id = ? WHERE name = ?";
				pstmt = db.getPreparedStatement(sql);
				pstmt.setString(1, vm.getDescription());
				pstmt.setString(2, vm.getUpd_id());
				pstmt.setString(3, vm.getName());
				pstmt.execute();
			} else {
				sql = "INSERT INTO vm(name, description, reg_date, reg_id) VALUES (?, ?, SYSDATE(), ?)";
				pstmt = db.getPreparedStatement(sql);
				pstmt.setString(1, vm.getName());
				pstmt.setString(2, vm.getDescription());
				pstmt.setString(3, vm.getReg_id());
				pstmt.execute();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try { rs.close(); } catch(Exception e) {}
			try { pstmt.close(); } catch(Exception e) {}
			db.close();
		}
	}
}
