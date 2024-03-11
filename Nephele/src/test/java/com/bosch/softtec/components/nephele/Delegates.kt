/*
 * Copyright (c) 2019 Bosch SoftTec GmbH All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Bosch
 * SoftTec GmbH ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Bosch SoftTec (BSOT).
 *
 * BSOT MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. BSOT SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */


package com.bosch.softtec.components.nephele

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Andre Weber
 * @since  2019-04-25
 */
class ReflectiveReadWriteProperty<T>(private val fieldName: String) : ReadWriteProperty<BaseTest<*>, T>
{
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: BaseTest<*>, property: KProperty<*>): T
    {
        return thisRef.getFieldValue<Any>(fieldName) as T
    }

    override fun setValue(thisRef: BaseTest<*>, property: KProperty<*>, value: T)
    {
        thisRef.setFieldValue(fieldName, value)
    }
}

class ReflectiveReadOnlyProperty<T>(private val fieldName: String) : ReadOnlyProperty<BaseTest<*>, T>
{
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: BaseTest<*>, property: KProperty<*>): T
    {
        return thisRef.getFieldValue<Any>(fieldName) as T
    }
}
