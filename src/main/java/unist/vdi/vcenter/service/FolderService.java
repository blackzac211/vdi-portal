package unist.vdi.vcenter.service;

import java.util.List;

import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.bindings.StubFactory;
import com.vmware.vcenter.Folder;
import com.vmware.vcenter.FolderTypes.FilterSpec.Builder;
import com.vmware.vcenter.FolderTypes.Summary;

public class FolderService {
	public static List<Summary> getAllFolders(StubFactory stubFactory, StubConfiguration sessionStubConfig) {
		// Get the folder
		Folder folderService = stubFactory.createStub(Folder.class, sessionStubConfig);
		Builder builder = new Builder();

		List<Summary> list = folderService.list(builder.build());

		return list;
	}
}
