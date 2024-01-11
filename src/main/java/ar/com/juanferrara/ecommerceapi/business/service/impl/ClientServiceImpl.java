package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.business.mapper.PageResponseMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.user.ClientDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.service.ClientService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.ClientDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Address;
import ar.com.juanferrara.ecommerceapi.domain.entity.Client;
import ar.com.juanferrara.ecommerceapi.domain.entity.User;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import ar.com.juanferrara.ecommerceapi.persistence.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientDtoMapper clientDtoMapper;


    @Override
    public ClientDTO createClient(ClientDTO clientDTO, User userAssigned) {
        Client client = clientDtoMapper.toEntity(clientDTO);
        client.setUser(userAssigned);

        return clientDtoMapper.toDto(clientRepository.save(client));
    }

    @Override
    public ClientDTO getClientByDni(Long dni) {
        Client client = clientRepository.findById(dni)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        ClientDTO clientToReturn = clientDtoMapper.toDto(client);
        clientToReturn.setUserId(client.getUser().getId());

        return clientToReturn;
    }

    @Override
    public ClientDTO getClientByEmail(String email) {
        Client client = clientRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        ClientDTO clientToReturn = clientDtoMapper.toDto(client);
        clientToReturn.setUserId(client.getUser().getId());

        return clientToReturn;
    }

    @Override
    public Set<Address> getAddressesOfClient(Long idClient) {
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        return client.getAddresses();
    }

    @Override
    public PageResponse listAllClients(Pageable pageable) {
        Page<Client> employeePage = clientRepository.findAll(pageable);
        return PageResponseMapper.convertToPageResponse(employeePage.map(clientDtoMapper::toClientDto));
    }

    @Override
    public PageResponse findClients(Pageable pageable, ClientDTO clientExample) {
        Client clientExampleToFind = clientDtoMapper.toEntity(clientExample);

        Example<Client> exampleClient = Example.of(clientExampleToFind, ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase());

        Page<Client> pageClient = clientRepository.findAll(exampleClient, pageable);
        return PageResponseMapper.convertToPageResponse(pageClient);
    }

    @Override
    public ClientDTO updateClient(Long dni, ClientDTO clientDTO) {
        Client client = clientRepository.findById(dni)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        clientDtoMapper.updateClient(client, clientDtoMapper.toEntity(clientDTO));

        clientRepository.save(client);

        return clientDtoMapper.toDto(client);
    }

    @Override
    public ClientDTO disableClient(Long dni) {
        Client client = clientRepository.findById(dni)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        client.getUser().setUserEnabled(false);
        clientRepository.save(client);

        return clientDtoMapper.toDto(client);
    }
}
