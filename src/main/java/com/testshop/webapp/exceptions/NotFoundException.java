package com.testshop.webapp.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;

    private String messaggio = "Elemento Ricercato non trovato!";

    public NotFoundException()
    {
        super();
    }

    public NotFoundException(String messaggio)
    {
        super(messaggio);
        this.messaggio = messaggio;
    }

}
