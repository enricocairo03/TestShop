package com.testshop.webapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class InfoMsg implements Serializable
{
    private static final long serialVersionUID = 14L;

    public LocalDate data;

    public String message;

}
