/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client.model.profile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amee.client.AmeeException;
import com.amee.client.model.base.AmeeCategory;
import com.amee.client.model.base.AmeeConstants;
import com.amee.client.model.base.AmeeItem;
import com.amee.client.model.base.AmeeObject;
import com.amee.client.model.base.AmeeObjectReference;
import com.amee.client.model.base.AmeeObjectType;
import com.amee.client.model.data.AmeeDataItem;
import com.amee.client.service.AmeeObjectFactory;
import com.amee.client.util.Choice;

public class AmeeProfileCategory extends AmeeCategory implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5992470044906506904L;
	private AmeeObjectReference profileRef;
    private AmeeObjectReference dataCategoryRef;

    private BigDecimal amount = AmeeConstants.ZERO;
    private String unit = "kg/year";
    
    public AmeeProfileCategory() {
        super();
    }

    public AmeeProfileCategory(AmeeObjectReference ref) {
        super(ref);
    }

    public AmeeProfileCategory(String path, AmeeObjectType objectType) {
        super(path, objectType);
    }

    public void populate(AmeeProfileCategory copy) {
        super.populate(copy);
        copy.setProfileRef(profileRef);
        copy.setDataCategoryRef(dataCategoryRef);
        copy.setAmount(amount);
        copy.setAmountUnit(unit);
    }

    @Override
	public AmeeObject getCopy() {
        AmeeProfileCategory copy = new AmeeProfileCategory();
        populate(copy);
        return copy;
    }

    @Override
	public void setParentRef() {
        String parentUri = getObjectReference().getParentUri();
        if (parentUri.equals(getProfileRef().getUri())) {
            setParentRef(getProfileRef());
        } else {
            setParentRef(new AmeeObjectReference(parentUri, AmeeObjectType.PROFILE_CATEGORY));
        }
    }

    @Override
	public AmeeCategory getNewChildCategory(AmeeObjectReference ref) {
        return new AmeeProfileCategory(ref);
    }

    @Override
	public AmeeObjectType getChildCategoryObjectType() {
        return AmeeObjectType.PROFILE_CATEGORY;
    }

    @Override
	public AmeeItem getNewChildItem(AmeeObjectReference ref) {
        return new AmeeProfileItem(ref);
    }

    @Override
	public AmeeObjectType getChildItemObjectType() {
        return AmeeObjectType.PROFILE_ITEM;
    }

    public List<AmeeProfileCategory> getProfileCategories() throws AmeeException {
        List<AmeeProfileCategory> profileCategories = new ArrayList<AmeeProfileCategory>();
        for (AmeeCategory category : super.getCategories()) {
            profileCategories.add((AmeeProfileCategory) category);
        }
        return profileCategories;
    }

    public List<AmeeProfileItem> getProfileItems() throws AmeeException {
        List<AmeeProfileItem> profileItems = new ArrayList<AmeeProfileItem>();
        for (AmeeItem item : super.getItems()) {
            profileItems.add((AmeeProfileItem) item);
        }
        return profileItems;
    }

    /**
     * Add a new AmeeProfileItem to this AmeeProfileCategory.
     *
     * @param dataItemUid the UID of the AmeeDataItem to base the new AmeeProfileItem on
     * @return the new AmeeProfileItem
     * @throws AmeeException
     */
    public AmeeProfileItem addProfileItem(String dataItemUid) throws AmeeException {
        return AmeeObjectFactory.getInstance().addProfileItem(this, dataItemUid, null);
    }

    /**
     * Add a new Profile Item to this Profile Category.
     *
     * @param dataItem the AmeeDataItem to base the new AmeeProfileItem on
     * @return the new AmeeProfileItem
     * @throws AmeeException
     */
    public AmeeProfileItem addProfileItem(AmeeDataItem dataItem) throws AmeeException {
        return addProfileItem(dataItem.getUid());
    }

    /**
     * Add a new Profile Item to this Profile Category.
     *
     * @param dataItemUid the UID of the AmeeDataItem to base the new AmeeProfileItem on
     * @param values
     * @return the new AmeeProfileItem
     * @throws AmeeException
     */
    public AmeeProfileItem addProfileItem(String dataItemUid, List<Choice> values) throws AmeeException {
        return AmeeObjectFactory.getInstance().addProfileItem(this, dataItemUid, values);
    }

    /**
     * Add a new Profile Item to this Profile Category.
     *
     * @param dataItem the AmeeDataItem to base the new AmeeProfileItem on
     * @param values
     * @return the new AmeeProfileItem
     * @throws AmeeException
     */
    public AmeeProfileItem addProfileItem(AmeeDataItem dataItem, List<Choice> values) throws AmeeException {
        return addProfileItem(dataItem.getUid(), values);
    }

    public AmeeObjectReference getProfileRef() {
        if (profileRef == null) {
            setProfileRef();
        }
        return profileRef;
    }

    public void setProfileRef(AmeeObjectReference profileRef) {
        if (profileRef != null) {
            this.profileRef = profileRef;
        }
    }

    public void setProfileRef() {
        String ref = getObjectReference().getUriFirstTwoParts();
        if (ref != null) {
            setProfileRef(new AmeeObjectReference(ref, AmeeObjectType.PROFILE));
        }
    }

    public AmeeObjectReference getDataCategoryRef() {
        if (dataCategoryRef == null) {
            setDataCategoryRef();
        }
        return dataCategoryRef;
    }

    public void setDataCategoryRef(AmeeObjectReference dataCategoryRef) {
        if (dataCategoryRef != null) {
            this.dataCategoryRef = dataCategoryRef;
        }
    }

    public void setDataCategoryRef() {
        setProfileRef(
                new AmeeObjectReference(
                        "/data/" + getObjectReference().getUriExceptFirstTwoParts(),
                        AmeeObjectType.DATA_CATEGORY));
    }

    public BigDecimal getAmount() throws AmeeException {        
        if (!isFetched()) {
            fetch();
    }
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.amount = amount;
        }
    }

    public void setAmount(String amount) {
        if (amount != null) {
            BigDecimal newAmount = new BigDecimal(amount);
            newAmount = newAmount.setScale(AmeeConstants.SCALE, AmeeConstants.ROUNDING_MODE);
            if (newAmount.precision() > AmeeConstants.PRECISION) {
                // TODO: do something
            }
            setAmount(newAmount);
        }
    }

    public void setAmountUnit(String unit) {
        if (unit == null || unit.length() == 0)
            return;
        this.unit = unit;
    }

    public String getAmountUnit() {
        return unit;
    }

}