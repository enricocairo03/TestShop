package com.testshop.webapp.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateException extends Exception
{
    private static final long serialVersionUID = 2L;

    private String messaggio;

    public DuplicateException()
    {
        super();
    }

    public DuplicateException(String messaggio)
    {
        super(messaggio);
        this.messaggio = messaggio;
    }
}
