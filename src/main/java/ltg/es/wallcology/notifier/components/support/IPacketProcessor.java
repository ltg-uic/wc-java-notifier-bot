package ltg.es.wallcology.notifier.components.support;

import org.jivesoftware.smack.packet.Message;

public interface IPacketProcessor {

	public void processPacket(Message packet);

}