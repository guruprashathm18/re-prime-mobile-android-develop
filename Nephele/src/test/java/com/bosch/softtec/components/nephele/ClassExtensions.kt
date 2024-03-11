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

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier


/**
 * @author Andre Weber
 * @since  2019-04-29
 */
fun Any.callMethodByName(methodName: String, argumentClasses: Array<Class<out Any>>?, vararg arguments: Any) {
    val declaredMethod = findDeclaredMethod(methodName, argumentClasses)

    try {
        declaredMethod.isAccessible = true
        declaredMethod.invoke(this, *arguments)
    } finally {
        declaredMethod.isAccessible = false
    }
}

private fun Any.findDeclaredMethod(
    methodName: String,
    argumentClasses: Array<Class<out Any>>?
): Method {
    val declaredMethods = javaClass.declaredMethods.filter { it.name.contains(methodName) }

    return when {
        declaredMethods.isEmpty() -> throw IllegalArgumentException(
            "Could not find method with name '$methodName' in class ${javaClass.simpleName}"
        )

        declaredMethods.size == 1 -> declaredMethods[0]

        else -> if (argumentClasses == null) {
            throw IllegalArgumentException(
                "Ambiguous method name '$methodName', please specify argumentClasses explicitly"
            )
        } else {
            javaClass.getDeclaredMethod(methodName, *argumentClasses)
        }
    }
}

/**
 * Gets the value of the field with the specified fieldName from the specified object.
 *
 * @param fieldName
 * the fieldName of the field.
 * @param value
 * the value to set.
 * @throws NoSuchFieldException
 * if the requested field can not be found.
 * @throws IllegalAccessException
 * if this field is not accessible.
 * @throws IllegalArgumentException when no field with the specified name could be found
 */
@Throws(NoSuchFieldException::class, IllegalAccessException::class)
fun Any.setFieldValue(fieldName: String, value: Any?) {
    val field = findFieldRecursively(fieldName)

    var wasFinal = false
    try {
        field.isAccessible = true
        wasFinal = field.removeFinalOperator()
        field.set(this, value)
    } finally {
        if (wasFinal) field.addFinalOperator()
        field.isAccessible = false
    }

}

// this might break with a later version of java; but good enough for testing purposes
private fun Field.removeFinalOperator(): Boolean {
    val wasFinal = modifiers and Modifier.FINAL == 1

    val modifiersField = findFieldRecursively("modifiers")
    try {
        modifiersField.isAccessible = true
        modifiersField.set(this, modifiers and Modifier.FINAL.inv())
    } finally {
        modifiersField.isAccessible = false
    }

    return wasFinal
}

// this might break with a later version of java; but good enough for testing purposes
private fun Field.addFinalOperator() {
    val modifiersField = findFieldRecursively("modifiers")
    try {
        modifiersField.isAccessible = true
        modifiersField.set(this, modifiers or Modifier.FINAL)
    } finally {
        modifiersField.isAccessible = false
    }
}


/**
 * Gets the value of the field with the specified fieldName from the specified object.
 *
 * @param fieldName
 * the fieldName of the field.
 * @return the value of the Field with the given fieldName from the given object.
 * @throws IllegalAccessException if this field is not accessible.
 * @throws IllegalArgumentException when no field with the specified name could be found
 */
@Suppress("UNCHECKED_CAST")
@Throws(IllegalAccessException::class)
fun <T : Any> Any.getFieldValue(fieldName: String): T? {
    val field = findFieldRecursively(fieldName)

    try {
        field.isAccessible = true
        return field.get(this) as T?
    } finally {
        field.isAccessible = false
    }
}

/**
 * @throws IllegalArgumentException when no field with the specified name could be found.
 */
private fun Any.findFieldRecursively(fieldName: String): Field {
    var field: Field? = null

    var clazz: Class<*>? = javaClass
    while (field == null && clazz != null) {
        try {
            field = clazz.getDeclaredField(fieldName)
        } catch (e: NoSuchFieldException) {
            clazz = clazz.superclass
        }
    }

    return field
        ?: throw IllegalArgumentException("Field '$fieldName' does not exist in class '${javaClass.simpleName}'")
}
