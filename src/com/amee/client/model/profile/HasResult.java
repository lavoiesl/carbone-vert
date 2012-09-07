package com.amee.client.model.profile;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.amee.client.AmeeException;

/**
 * @author James Smith
 */
public interface HasResult {

    /* Old interface */

    /* @deprecated Use {@link #getReturnValues()} instead, which
     *             supports multiple return values.
     */
    @Deprecated
    public BigDecimal getAmount() throws AmeeException;

    /* @deprecated Use {@link #getReturnValues()} instead, which
     *             supports multiple return values.
     */
    @Deprecated
    public String getAmountUnit() throws AmeeException;


    /* Multiple return value interface */

    public void addReturnValue(ReturnValue value);

    public ArrayList<ReturnValue> getReturnValues() throws AmeeException;

    public ReturnValue getDefaultReturnValue() throws AmeeException;

    public void addNote(ReturnNote note);

    public ArrayList<ReturnNote> getNotes() throws AmeeException;

}
