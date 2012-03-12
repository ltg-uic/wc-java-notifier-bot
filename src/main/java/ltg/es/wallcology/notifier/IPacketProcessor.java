package ltg.es.wallcology.notifier;

import org.jivesoftware.smack.packet.Message;

public interface IPacketProcessor {

	public void processPacket(Message packet);

}