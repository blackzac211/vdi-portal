package unist.vdi.common;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import unist.vdi.vcenter.service.VDIConnection;

@Component
public class SessionService {
	// 15분에 한번씩 vCenter 세션 확인
	@Scheduled(fixedDelay = 900000)
	public void task() {
		VDIConnection.initInstance();
	}
}
