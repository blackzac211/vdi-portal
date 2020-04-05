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
package vmware.samples.vcenter.vm.list;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.Option;

import com.vmware.vcenter.VM;
import com.vmware.vcenter.VMTypes.Summary;
import com.vmware.vcenter.vm.guest.Identity;

import unist.vdi.vcenter.service.CustomVM;

import com.vmware.vcenter.VMTypes.FilterSpec.Builder;
import vmware.samples.common.SamplesAbstractBase;
import vmware.samples.vcenter.helpers.ClusterHelper;
import vmware.samples.vcenter.helpers.DatacenterHelper;
import vmware.samples.vcenter.helpers.FolderHelper;

/**
 * Description: Demonstrates getting list of VMs present in vCenter
 *
 * Author: VMware, Inc.
 * Sample Prerequisites: vCenter 6.5+
 */
public class ListVMs extends SamplesAbstractBase {
    private VM vmService;
    private Identity identity;
    private String vmFolderName;
    private String datacenterName;
    private String clusterName;
    
    private String userId;
    private List<CustomVM> resList;
    private List<Summary> list;
    
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Define the options specific to this sample and configure the sample using
     * command-line arguments or a config file
     *
     * @param args command line arguments passed to the sample
     */
    
    protected void parseArgs(String[] args) {
    	System.out.println("\n");
    	System.out.println(df.format(Calendar.getInstance().getTime()) + " : parseArgs() start");
    	
        Option datacenterOption = Option.builder()
                .longOpt("datacenter")
                .desc("OPTIONAL: Specify the name of the Datacenter"
                        + " to list the Vms in it.")
                .argName("DATACENTER")
                .required(false)
                .hasArg()
                .build();
        Option vmFolderOption = Option.builder()
                .longOpt("vmfolder")
                .desc("OPTIONAL: Specify the name of the VM Folder to list the"
                        + " Vms in it.")
                .argName("VM FOLDER")
                .required(false)
                .hasArg()
                .build();
        Option clusterOption = Option.builder()
                .longOpt("cluster")
                .desc("OPTIONAL: Specify the name of the Cluster to list the"
                        + " Vms in it.")
                .argName("CLUSTER")
                .required(false)
                .hasArg()
                .build();
        List<Option> optionList = Arrays.asList(vmFolderOption, datacenterOption, clusterOption);

        super.parseArgs(optionList, args);
        vmFolderName = (String) parsedOptions.get("vmfolder");
        datacenterName = (String) parsedOptions.get("datacenter");
        clusterName = (String) parsedOptions.get("cluster");
        
        System.out.println(df.format(Calendar.getInstance().getTime()) + " : parseArgs() end");
    }

    protected void setup() throws Exception {
    	System.out.println(df.format(Calendar.getInstance().getTime()) + " : setup() start");
    	
        vmService = vapiAuthHelper.getStubFactory().createStub(VM.class, sessionStubConfig);
        identity = vapiAuthHelper.getStubFactory().createStub(Identity.class, sessionStubConfig);
        
        System.out.println(df.format(Calendar.getInstance().getTime()) + " : setup() end");
    }

    protected void run() throws Exception {
    	System.out.println(df.format(Calendar.getInstance().getTime()) + " : run() start");
    	
        Builder bldr = new Builder();
        if(null != this.datacenterName && !this.datacenterName.isEmpty()){
            bldr.setDatacenters(Collections.singleton(DatacenterHelper.
                  getDatacenter(this.vapiAuthHelper.getStubFactory(),
                          this.sessionStubConfig, this.datacenterName)));
        }
        if(null != this.clusterName && !this.clusterName.isEmpty()) {
            bldr.setClusters(Collections.singleton(ClusterHelper.getCluster(
                  this.vapiAuthHelper.getStubFactory(), sessionStubConfig,
                  this.clusterName)));
        }      
        if(null != this.vmFolderName && !this.vmFolderName.isEmpty())
        {
            bldr.setFolders(Collections.singleton(FolderHelper.getFolder(
                  this.vapiAuthHelper.getStubFactory(), sessionStubConfig,
                  this.vmFolderName)));
        }
        list = this.vmService.list(bldr.build());
		resList = new ArrayList<CustomVM>();
		
		for(Summary vmSummary : list) {
			if(vmSummary.getName().startsWith(userId + "-")) {
            	CustomVM temp = new CustomVM();
            	temp.setVm(vmSummary.getVm());
            	temp.setName(vmSummary.getName());
            	temp.setPowerState(vmSummary.getPowerState().toString());
            	try {
            		temp.setIpAddress(identity.get(vmSummary.getVm()).getIpAddress());
            	} catch(Exception e) {
            		temp.setIpAddress("");
            	}
            	resList.add(temp);
			}
        }
		
		System.out.println(df.format(Calendar.getInstance().getTime()) + " : run() end");
    }
    protected void cleanup() throws Exception {
    	
    }
    /*
    public List<CustomVM> getVMList(int nav, String userId) {
    	try {
    		String str = "--server "+VcenterAccount.server[nav]+" --username "+VcenterAccount.username[nav]+" --password "+VcenterAccount.password[nav]+" --skip-server-verification true";
    		String[] args = str.split(" ");
    		
    		this.userId = userId;
    		this.execute(args);
    		
	        return resList;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    */
}
