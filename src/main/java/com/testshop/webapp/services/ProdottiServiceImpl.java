package com.testshop.webapp.services;

import com.testshop.webapp.dtos.ProdottiDto;
import com.testshop.webapp.entities.Prodotti;
import com.testshop.webapp.repository.ProdottiRepository;
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
@Transactional(readOnly = true)
@CacheConfig(cacheNames = {"prodotti"})
@Log
public class ProdottiServiceImpl implements ProdottiService
{
    @Autowired
    private ProdottiRepository prodottiRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CacheManager cacheManager;

    @Override
    @Cacheable
    public List<ProdottiDto>SelByDescrizione(String descrizione)
    {
        String filter = descrizione.toLowerCase() + "%";

        List<Prodotti> prodotti = prodottiRepository.SelByDescrizioneLike(filter);

        return ConvertToDto(prodotti);
    }

    @Override
    @Cacheable
    public List<ProdottiDto> SelByDescrizione(String descrizione, Pageable pageable)
    {
        String filter =  "%" +descrizione.toLowerCase() + "%";

        List<Prodotti> prodotti = prodottiRepository.findByDescrizioneLike(filter, pageable);

        return ConvertToDto(prodotti);
    }

    @Override
    public Prodotti SelByIdProdotto2(Integer idProdotto)
    {
        return prodottiRepository.findByIdProdotto(idProdotto);
    }

    @Override
    @Cacheable(value = "prodotto", key = "#idProdotto", sync = true)
    public ProdottiDto SelByIdProdotto(Integer idProdotto)
    {
        Prodotti prodotti = this.SelByIdProdotto2(idProdotto);

        return this.ConvertToDto(prodotti);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "prodotti", allEntries = true),
            @CacheEvict(cacheNames = "prodotto", key = "#prodotto.idProdotto"),
    })
    public void DelProdotto(Prodotti prodotto){
        prodottiRepository.delete(prodotto);

    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "prodotti", allEntries = true),
            @CacheEvict(cacheNames = "prodotto", key = "#prodotto.idProdotto"),
    })
    public void InsProdotto(Prodotti prodotto)
    {
        prodotto.setDescrizione(prodotto.getDescrizione().toUpperCase());
        prodottiRepository.save(prodotto);

    }

    private ProdottiDto ConvertToDto(Prodotti prodotti)
    {
        ProdottiDto prodottiDto = null;

        if(prodotti != null)
        {
            prodottiDto = modelMapper.map(prodotti, ProdottiDto.class);
        }
        return prodottiDto;
    }

    private List<ProdottiDto> ConvertToDto(List<Prodotti> prodotti)
    {
        List<ProdottiDto> retVal = prodotti.stream().map(source -> modelMapper.map(source, ProdottiDto.class)).collect(Collectors.toList());

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
