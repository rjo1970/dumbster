package com.dumbster.smtp.eml;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import com.dumbster.smtp.MailMessage;

/**
 * An implementation of MailMessage to support lazy load of file messages.
 * <br/>
 * Each message is attached to a file but won't load the file until data is requested. 
 * @author daniel
 */
public class EMLMailMessage implements MailMessage {

    
    private File file;

    public EMLMailMessage(File file) {
        this.file = file;
    }

    /**
     * {@inheritDoc}
     * @see com.dumbster.smtp.MailMessage#getHeaderNames()
     */
    @Override
    public Iterator<String> getHeaderNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see com.dumbster.smtp.MailMessage#getHeaderValues(java.lang.String)
     */
    @Override
    public String[] getHeaderValues(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see com.dumbster.smtp.MailMessage#getFirstHeaderValue(java.lang.String)
     */
    @Override
    public String getFirstHeaderValue(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see com.dumbster.smtp.MailMessage#getBody()
     */
    @Override
    public String getBody() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Throws a OperationNotSupportedException as this implementation is read-only. 
     * @throws OperationNotSupportedException
     */
    @Override
    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException("EmlMailMessage is a read-only implementation of MailMessage");
    }

    /**
     * Throws a OperationNotSupportedException as this implementation is read-only. 
     * @throws OperationNotSupportedException
     */
    @Override
    public void appendHeader(String name, String value) {
        throw new UnsupportedOperationException("EmlMailMessage is a read-only implementation of MailMessage");
    }

    /**
     * Throws a OperationNotSupportedException as this implementation is read-only. 
     * @throws OperationNotSupportedException
     */
    @Override
    public void appendBody(String line) {
        throw new UnsupportedOperationException("EmlMailMessage is a read-only implementation of MailMessage");

    }

}
