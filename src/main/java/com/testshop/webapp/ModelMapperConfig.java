package com.testshop.webapp;

import com.testshop.webapp.dtos.ProdottiDto;
import com.testshop.webapp.entities.Prodotti;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig
{

    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.addMappings(prodottiMapping);

        modelMapper.addConverter(prodottiConverter);

        return modelMapper;
    }

    PropertyMap<Prodotti, ProdottiDto> prodottiMapping = new PropertyMap<Prodotti, ProdottiDto>()
    {
        protected void configure()
        {
            map().setDescrizione(source.getDescrizione());
        }

    };

    Converter<String, String> prodottiConverter = new Converter<String, String >()
    {
        @Override
        public String convert(MappingContext<String, String> context)
        {
            return context.getSource() == null ? "" : context.getSource().trim();
        }
    };


}
