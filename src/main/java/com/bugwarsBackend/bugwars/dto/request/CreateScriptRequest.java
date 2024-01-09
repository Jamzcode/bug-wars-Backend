package com.bugwarsBackend.bugwars.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateScriptRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String raw;
}