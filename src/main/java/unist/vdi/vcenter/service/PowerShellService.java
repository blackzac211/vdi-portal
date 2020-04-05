package unist.vdi.vcenter.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PowerShellService {
	
	
	private List<String> execRemote(String[] arguments) {
		try {
			ProcessBuilder builder = new ProcessBuilder(arguments);
			builder.redirectErrorStream(true);
			Process p = builder.start();
						
			// print result message
			p.getOutputStream().close();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			List<String> result = new ArrayList<String>();
			while ((line = stdout.readLine()) != null) {
				line = line.trim();
				if(line.startsWith("Ticket") ||
						line.startsWith("CfgFile") || 
						line.startsWith("Host") ||
						line.startsWith("Port") ||
						line.startsWith("SslThumbprint")) {
					result.add(line);
				}
			}
			stdout.close();
			// print error message
			BufferedReader stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((line = stderr.readLine()) != null) {
				System.err.println(line);
			}
			stderr.close();
			
			return result;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public HashMap<String, String> acquireMksTicket(String vmId) {
		HashMap<String, String> map = new HashMap<>();
		
		String cmd = "";
		map.put("vcenter", VCenterAccount.server);
		cmd = "$server = Connect-VIServer -Server "+VCenterAccount.server+" -User "+VCenterAccount.id+" -Password "+VCenterAccount.pwd+";";
		cmd += "$vm = get-vm -Id VirtualMachine-" + vmId+";";
		// cmd += "$vm.ExtensionData.AcquireMksTicket();";
		cmd += "$vm.ExtensionData.AcquireTicket('webmks');";
		cmd += "Disconnect-VIServer -Server $server -Force -Confirm:$false;";
		
		String[] args = new String[] { "powershell", cmd };
		List<String> list = execRemote(args);
		int index;
		for(int i = 0; i < list.size(); i++) {
			index = list.get(i).indexOf(":");
			map.put(list.get(i).substring(0, index).trim(), list.get(i).substring(index + 1, list.get(i).length()).trim());
		}
		return map;
	}
}
