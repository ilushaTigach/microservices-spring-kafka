package org.telatenko.addressserviceapi.dto;

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
@Schema(name = "Сущность адреса")
public class AddressDto {

    @Schema(description = "Идентификатор адреса", example = "38d42f1b-4b11-49a6-9106-6ee486fb15aa")
    private UUID id;

    @Schema(description = "Улица")
    private String street;

    @Schema(description = "Город")
    private String city;

    @Schema(description = "Индекс")
    private String zipCode;

}
