package com.testshop.webapp.services;

import com.testshop.webapp.dtos.FornitoriDto;
import com.testshop.webapp.entities.Fornitori;
import com.testshop.webapp.repository.FornitoriRepository;
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
@CacheConfig(cacheNames = "{fornitori}")
@Log
public class FornitoriServiceImpl implements FornitoriService
{
    @Autowired
    private FornitoriRepository fornitoriRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CacheManager cacheManager;

    @Override
    @Cacheable
    public List<FornitoriDto>SelByEmail(String email)
    {
        String filter = email.toLowerCase() + "%";

        List<Fornitori> fornitori = fornitoriRepository.SelByEmailLike(filter);

        return ConvertToDto(fornitori);
    }

    @Override
    @Cacheable
    public List<FornitoriDto>SelByEmail(String email , Pageable pageable)
    {
        String filter = email.toLowerCase() + "%";

        List<Fornitori> fornitori = fornitoriRepository.findByEmailLike(filter, pageable);

        return ConvertToDto(fornitori);
    }

    @Override
    @Cacheable
    public List<FornitoriDto>SelByTelefono(String telefono)
    {
        String filter = telefono.toLowerCase() + "%";

        List<Fornitori> fornitori = fornitoriRepository.SelByTelefonoLike(filter);

        return ConvertToDto(fornitori);
    }

    @Override
    @Cacheable
    public List<FornitoriDto>SelByTelefono(String telefono, Pageable pageable)
    {
        String filter = telefono.toLowerCase() + "%";

        List<Fornitori> fornitori = fornitoriRepository.findByTelefonoLike(filter, pageable);

        return ConvertToDto(fornitori);
    }

    @Override
    public Fornitori SelByIdFornitore2(Integer idFornitore)
    {
        return fornitoriRepository.findByIdFornitore(idFornitore);
    }

    @Override
    @Cacheable(value = "fornitore", key = "#idFornitore", sync = true)
    public FornitoriDto SelByIdFornitore(Integer idFornitore)
    {
        if(idFornitore == null)
        {
            return null;
        }

        Fornitori fornitore = this.SelByIdFornitore2(idFornitore);

        return this.ConvertToDto(fornitore);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "fornitori", allEntries = true),
            @CacheEvict(cacheNames = "fornitore", key = "#fornitore.idFornitore"),
    })
    public void DelFornitore(Fornitori fornitore)
    {
        fornitoriRepository.delete(fornitore);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "fornitori", allEntries = true),
            @CacheEvict(cacheNames = "fornitore", key = "#fornitore.idFornitore"),
    })
    public void InsFornitore(Fornitori fornitore)
    {
        fornitore.setEmail(fornitore.getEmail().toUpperCase());
        fornitore.setTelefono(fornitore.getTelefono().toUpperCase());

        fornitoriRepository.save(fornitore);
    }

    private FornitoriDto ConvertToDto(Fornitori fornitore)
    {
        FornitoriDto fornitoriDto = null;

        if(fornitore != null)
        {
            fornitoriDto = modelMapper.map(fornitore, FornitoriDto.class);
        }
        return fornitoriDto;
    }

    private List<FornitoriDto> ConvertToDto(List<Fornitori> fornitori)
    {
        List<FornitoriDto> retVal = fornitori.stream().map(source -> modelMapper.map(source, FornitoriDto.class)).collect(Collectors.toList());
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
