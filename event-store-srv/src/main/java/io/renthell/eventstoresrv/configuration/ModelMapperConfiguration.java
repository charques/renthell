package io.renthell.eventstoresrv.configuration;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<String, Date> toStringDate = new AbstractConverter<String, Date>() {
            @Override
            protected Date convert(String source) {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = null;
                try {
                    date = format.parse(source);
                } catch (ParseException e) {
                    return null;
                }

                return date;
            }
        };
        modelMapper.addConverter(toStringDate);
        return modelMapper;
    }
}
