package org.telatenko.userserviceapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Value
@Builder
@Jacksonized
@Schema(name = "Сущность жильца")
public class UserDto {

    @Schema(description = "Идентификатор жильца", example = "38d42f1b-4b11-49a6-9106-6ee486fb15aa")
    private UUID id;

    @Schema(description = "ФИО жильца")
    private String name;

    @Schema(description = "email жильца")
    private String email;

    @Schema(description = "Идентификатор адреса, по которому проживает жилец", example = "38d42f1b-4b11-49a6-9106-6ee486fb15aa")
    private UUID addressId;
}
