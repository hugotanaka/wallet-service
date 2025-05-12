package com.hugotanaka.wallet.adapter.output.persistence.mapper.converter;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Named("MapperConverter")
public class MapperConverter {

    @Named("StringToUUID")
    public UUID convertStringToUUID(String value) {
        return value != null ? UUID.fromString(value) : null;
    }

    @Named("UUIDToString")
    public String convertUUIDToString(UUID value) {
        return value != null ? value.toString() : null;
    }
}
