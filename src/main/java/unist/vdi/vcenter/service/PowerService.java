package unist.vdi.vcenter.service;

import com.vmware.vcenter.vm.Power;
import com.vmware.vcenter.vm.PowerTypes;

public class PowerService {
	private Power vmPowerService;
    
    public void powerOn(String vmId, VDIConnection conn) throws Exception {
    	vmPowerService = conn.getVapiAuthHelper().getStubFactory().createStub(Power.class, conn.getSessionStubConfig());
    	PowerTypes.Info powerInfo = vmPowerService.get(vmId);

        if(PowerTypes.State.POWERED_OFF.equals(powerInfo.getState())) {
        	vmPowerService.start(vmId);
        }
    }
    
    public void powerOff(String vmId, VDIConnection conn) throws Exception {
    	vmPowerService = conn.getVapiAuthHelper().getStubFactory().createStub(Power.class, conn.getSessionStubConfig());
    	PowerTypes.Info powerInfo = vmPowerService.get(vmId);

        if(PowerTypes.State.POWERED_ON.equals(powerInfo.getState())) {
        	vmPowerService.stop(vmId);
        }
    }
    
    public void reset(String vmId, VDIConnection conn) throws Exception {
    	vmPowerService = conn.getVapiAuthHelper().getStubFactory().createStub(Power.class, conn.getSessionStubConfig());
    	PowerTypes.Info powerInfo = vmPowerService.get(vmId);

        if(PowerTypes.State.POWERED_ON.equals(powerInfo.getState())) {
        	vmPowerService.reset(vmId);
        }
    }
}
