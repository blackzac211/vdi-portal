package unist.vdi.vcenter.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import com.vmware.vcenter.vm.guest.Power;

import vmware.samples.common.SamplesAbstractBase;

public class GuestPowerLifeCycle extends SamplesAbstractBase {
	private Power power;
    private String vmId;
    
    @Override
    protected void parseArgs(String[] args) {
        List<Option> optionList = new ArrayList<Option>();
        super.parseArgs(optionList, args);
    }

    @Override
    protected void setup() throws Exception {
        power = vapiAuthHelper.getStubFactory().createStub(Power.class, sessionStubConfig);
    }

    @Override
    protected void run() throws Exception {
    	power.reboot(vmId);
    }

    @Override
    protected void cleanup() throws Exception {
    	
    }

    public void restart(String[] args, String vmId) throws Exception {
    	this.vmId = vmId;
    	this.execute(args);
    }
    
    public static void main(String[] args) throws Exception {
        // new GuestPowerLifeCycle("aaa").execute(args);
    }

}
