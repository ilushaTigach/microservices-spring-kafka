package org.telatenko.addressserviceapi.resources.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.telatenko.addressserviceapi.resources.AddressResource;

@FeignClient(name = "address-service", url = "http://localhost:8080")
public interface AddressClient extends AddressResource {
}
