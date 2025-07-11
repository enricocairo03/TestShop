package com.testshop.webapp.services;

import com.testshop.webapp.dtos.OrdiniDto;
import com.testshop.webapp.entities.Clienti;
import com.testshop.webapp.entities.Ordini;
import com.testshop.webapp.entities.Pagamenti;
import com.testshop.webapp.repository.OrdiniRepository;
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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@CacheConfig(cacheNames = {"ordini"})
@Log
public class OrdiniServiceImpl implements OrdiniService
{
    @Autowired
    private OrdiniRepository ordiniRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CacheManager cacheManager;

    @Override
    @Cacheable
    public List<OrdiniDto>SelByStato(String stato)
    {
        String filter = stato.toLowerCase() + "%";

        List<Ordini> ordini = ordiniRepository.SelByStatoLike(filter);

        return ConvertToDto(ordini);
    }

    @Override
    @Cacheable
    public  List<OrdiniDto> SelByStato(String stato, Pageable pageable)
    {
        String filter = stato.toLowerCase() + "%";

        List<Ordini> ordini = ordiniRepository.findByStatoLike(filter, pageable);

        return ConvertToDto(ordini);
    }

    @Override
    public Ordini SelByIdOrdine2(Integer idOrdine)
    {
        return ordiniRepository.findByIdOrdine(idOrdine);
    }

    @Override
    @Cacheable(value = "ordine", key = "#idOrdine", sync = true)
    public OrdiniDto SelByIdOrdine(Integer idOrdine)
    {
        if(idOrdine == null){
            return null;
        }
        Ordini ordine = this.SelByIdOrdine2(idOrdine);

        return this.ConvertToDto(ordine);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "ordini", allEntries = true),
            @CacheEvict(cacheNames = "ordine", key = "#ordine.idOrdine"),
    })
    public void DelOrdini(Ordini ordine){
        ordiniRepository.delete(ordine);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "ordini", allEntries = true),
            @CacheEvict(cacheNames = "ordine", key = "#ordine.idOrdine"),
    })
    public void InsOrdini(Ordini ordine)
    {
        ordine.setStato(ordine.getStato().toUpperCase());
        ordiniRepository.save(ordine);
    }

    private OrdiniDto ConvertToDto(Ordini ordini)
    {
        OrdiniDto ordiniDto = null;

        if(ordini != null)
        {
            ordiniDto = modelMapper.map(ordini, OrdiniDto.class);
        }
        return ordiniDto;
    }
    private List<OrdiniDto> ConvertToDto(List<Ordini> ordini)
    {
        List<OrdiniDto> retVal = ordini.stream().map(source -> modelMapper.map(source, OrdiniDto.class)).collect(Collectors.toList());
        return retVal;
    }
    @Override
    public void CleanCaches()
    {
        Collection<String> items = cacheManager.getCacheNames();

        items.forEach((item)->{
            log.info(String.format("Eliminazione cache '%s'", item));

            cacheManager.getCache(item).clear();
        });
    }



}
