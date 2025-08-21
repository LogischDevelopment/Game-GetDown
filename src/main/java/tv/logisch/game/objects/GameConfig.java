package tv.logisch.game.objects;

import lombok.Getter;
import lombok.experimental.Accessors;
import tv.logisch.api.objects.ServerProperty;
import tv.logisch.game.GetDown;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public class GameConfig {

    private UUID hostUUID;
    private String hostName;

    public GameConfig initialize() {
        String service = System.getenv("service-name");
        ServerProperty[] properties = GetDown.instance().logiAPI().minecraftCloud().getServerProperties(service.split("-")[0], Integer.parseInt(service.split("-")[1]));
        for (ServerProperty property : properties) {
            if (property.getName().equals("host")) {
                this.hostUUID = UUID.fromString(property.getValue());
            } else if (property.getName().equals("host_name")) {
                this.hostName = property.getValue();
            }
        }
        return this;
    }

}
