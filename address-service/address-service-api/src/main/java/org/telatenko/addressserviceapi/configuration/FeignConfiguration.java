package org.telatenko.addressserviceapi.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.telatenko.addressserviceapi.resources.client.AddressClient;

@Configuration
@EnableFeignClients(basePackageClasses = {AddressClient.class})
public class FeignConfiguration {
}
