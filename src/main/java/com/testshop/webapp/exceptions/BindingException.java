package com.testshop.webapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BindingException extends Exception
{
    private static final long serialVersionUID = 3L;

    private String messaggio;

    public BindingException()
    {
        super();
    }

    public BindingException(String messaggio)
    {
        super(messaggio);
        this.messaggio = messaggio;
    }
}
