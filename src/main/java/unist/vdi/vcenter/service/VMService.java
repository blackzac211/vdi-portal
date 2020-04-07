package unist.vdi.vcenter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vmware.vcenter.VM;
import com.vmware.vcenter.VMTypes.Summary;
import com.vmware.vcenter.VMTypes.FilterSpec.Builder;
import com.vmware.vcenter.vm.guest.Identity;

import vmware.samples.vcenter.helpers.ClusterHelper;
import vmware.samples.vcenter.helpers.DatacenterHelper;
import vmware.samples.vcenter.helpers.FolderHelper;

public class VMService {
	
	public String getVMName(String vmId, VDIConnection conn) throws Exception {
		VM vm = conn.getVapiAuthHelper().getStubFactory().createStub(VM.class, conn.getSessionStubConfig());
		return vm.get(vmId).getName();
	}
	
	public List<CustomVM> getVMList(String userId, VDIConnection conn) {
		try {
			String vmFolderName = "";
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
			if (null != vmFolderName && !vmFolderName.isEmpty()) {
				bldr.setFolders(Collections.singleton(FolderHelper.getFolder(conn.getVapiAuthHelper().getStubFactory(),
						conn.getSessionStubConfig(), vmFolderName)));
			}
			List<Summary> list = vm.list(bldr.build());
			List<CustomVM> resList = new ArrayList<CustomVM>();

			for (Summary vmSummary : list) {
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
}
