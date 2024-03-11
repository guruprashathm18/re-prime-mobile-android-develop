package com.royalenfield.extensions

import com.bosch.softtec.micro.tenzing.client.model.Trip

/**
 * @author Markus Hettig
 * @since  2019-11-11
 */

/**
 * Returns all imageIds for a given Trip.
 */
val Trip.imageIds: List<String>
    get() = links
            ?.filter { link -> link.rel == "image" }
            ?.map { link -> link.href }
            ?.toList()
            ?: emptyList()
