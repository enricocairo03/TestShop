package com.testshop.webapp.services;

import com.testshop.webapp.dtos.ClientiDto;
import com.testshop.webapp.entities.Clienti;
import com.testshop.webapp.repository.ClientiRepository;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@CacheConfig(cacheNames = {"clienti"})
@Log
public class ClientiServiceImpl implements ClientiService {

    @Autowired
    private ClientiRepository clientiRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CacheManager cacheManager;

    @Override
    @Cacheable
    public List<ClientiDto> SelByTel(String telefono) {
        String filter = telefono.toLowerCase() + "%";

        List<Clienti> clienti = clientiRepository.SelByTelefonoLike(filter);

        return convertToDtoList(clienti);
    }

    @Override
    @Cacheable
    public List<ClientiDto> SelByTel(String telefono, Pageable pageable) {
        String filter = telefono.toLowerCase() + "%";

        List<Clienti> clienti = clientiRepository.SelByTelefonoLike(filter);

        return convertToDtoList(clienti);
    }

    @Override
    @Cacheable
    public Clienti SelByIdCliente2(Integer idCliente) {
        return clientiRepository.findByIdCliente(idCliente);
    }

    @Override
    @Cacheable(value = "cliente", key = "#idCliente", sync = true)
    public ClientiDto SelByIdCliente(Integer idCliente) {
        if (idCliente == null) {
            return null;
        }
        Clienti cliente = this.SelByIdCliente2(idCliente);
        return convertToDto(cliente);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "ordini", allEntries = true),
            @CacheEvict(cacheNames = "ordine", key = "#cliente.idCliente"),
    })
    public void DelClienti(Clienti cliente) {
        clientiRepository.delete(cliente);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "clienti", allEntries = true),
            @CacheEvict(cacheNames = "cliente", key = "#cliente.idCliente"),
    })
    public void InsClienti(Clienti cliente) {
        cliente.setTelefono(cliente.getTelefono().toUpperCase());
        clientiRepository.save(cliente);
    }

    private ClientiDto convertToDto(Clienti cliente) {
        if (cliente != null) {
            return modelMapper.map(cliente, ClientiDto.class);
        }
        return null;
    }

    private List<ClientiDto> convertToDtoList(List<Clienti> clienti) {
        return clienti.stream()
                .map(c -> modelMapper.map(c, ClientiDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void CleanCaches() {
        Collection<String> items = cacheManager.getCacheNames();

        items.forEach(item -> {
            log.info(String.format("Eliminazione cache '%s'", item));
            cacheManager.getCache(item).clear();
        });
    }
}
