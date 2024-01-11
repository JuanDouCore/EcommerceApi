package ar.com.juanferrara.ecommerceapi.business.mapper.user;

import ar.com.juanferrara.ecommerceapi.business.mapper.GenericMapper;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.ClientDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Client;
import ar.com.juanferrara.ecommerceapi.domain.entity.Employee;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ClientDtoMapper  {

    @Mapping(target = "userId", source = "client.user.id")
    @Mapping(target = "email", source = "client.user.email")
    ClientDTO toClientDto(Client client);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dni" , ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "names", source = "source.names")
    @Mapping(target = "lastName", source = "source.lastName")
    @Mapping(target = "birthDate", source = "source.birthDate")
    @Mapping(target = "addresses", source = "source.addresses")
    Client updateClient(Client target, Client source);

    Client toEntity(ClientDTO clientDTO);

    @Mapping(target = "userId", source = "client.user.id")
    @Mapping(target = "email", source = "client.user.email")
    ClientDTO toDto(Client client);
}
