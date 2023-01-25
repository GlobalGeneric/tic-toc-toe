package org.paribas.fortis.controller.dto;

import lombok.Data;
import org.paribas.fortis.model.Player;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
