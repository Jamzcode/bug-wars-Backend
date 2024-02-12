package com.bugwarsBackend.bugwars.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class GameRequest {
    @NotBlank
    private List<Long> scriptIds;

    @NotBlank
    private Integer mapId;
}
