package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.ClientDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Address;
import ar.com.juanferrara.ecommerceapi.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ClientService {

    ClientDTO createClient(ClientDTO clientDTO, User userAssigned);
    ClientDTO getClientByDni(Long dni);
    ClientDTO getClientByEmail(String email);
    Set<Address> getAddressesOfClient(Long idClient);
    PageResponse listAllClients(Pageable pageable);
    PageResponse findClients(Pageable pageable, ClientDTO clientExample);
    ClientDTO updateClient(Long dni, ClientDTO clientDTO);
    ClientDTO disableClient(Long dni);
}
