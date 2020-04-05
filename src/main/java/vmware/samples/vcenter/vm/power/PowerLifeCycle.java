/*
 * *******************************************************
 * Copyright VMware, Inc. 2017.  All Rights Reserved.
 * SPDX-License-Identifier: MIT
 * *******************************************************
 *
 * DISCLAIMER. THIS PROGRAM IS PROVIDED TO YOU "AS IS" WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, WHETHER ORAL OR WRITTEN,
 * EXPRESS OR IMPLIED. THE AUTHOR SPECIFICALLY DISCLAIMS ANY IMPLIED
 * WARRANTIES OR CONDITIONS OF MERCHANTABILITY, SATISFACTORY QUALITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package vmware.samples.vcenter.vm.power;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import com.vmware.vcenter.vm.Power;
import com.vmware.vcenter.vm.PowerTypes;

import vmware.samples.common.SamplesAbstractBase;


public class PowerLifeCycle extends SamplesAbstractBase {

    private Power vmPowerService;
    private String vmId;
    private int mode;

    @Override
    protected void parseArgs(String[] args) {
        List<Option> optionList = new ArrayList<Option>();
        super.parseArgs(optionList, args);
    }

    @Override
    protected void setup() throws Exception {
        vmPowerService = vapiAuthHelper.getStubFactory().createStub(Power.class, sessionStubConfig);
    }

    @Override
    protected void run() throws Exception {
        // Get the vm power state
        PowerTypes.Info powerInfo = vmPowerService.get(vmId);

        switch(mode) {
        case 0:	// turn off
        	if(PowerTypes.State.POWERED_ON.equals(powerInfo.getState())) {
            	this.vmPowerService.stop(vmId);
        	}
        	break;
        case 1:	// turn on
        	if (PowerTypes.State.POWERED_OFF.equals(powerInfo.getState())) {
            	this.vmPowerService.start(vmId);
            }
        	break;
        }
    }

    @Override
    protected void cleanup() throws Exception {
    	
    }
    /*
    public void powerOnOff(int nav, String vmId, int mode) throws Exception {
    	String str = "--server "+VcenterAccount.server[nav]+" --username "+VcenterAccount.username[nav]+" --password "+VcenterAccount.password[nav]+" --skip-server-verification true";
		String[] args = str.split(" ");
		
		this.vmId = vmId;
    	this.mode = mode;
    	this.execute(args);
    }
    */
}
