package de.westnordost.streetcomplete.data.osm.edits.update_tags

import kotlinx.serialization.Serializable

@Serializable
data class StringMapEntryAdd(val key: String, val value: String) : StringMapEntryChange {

    override fun toString() = "ADD \"$key\"=\"$value\""
    override fun conflictsWith(map: Map<String, String>) = map.containsKey(key) && map[key] != value
    override fun applyTo(map: MutableMap<String, String>) { map[key] = value }
    override fun reversed() = StringMapEntryDelete(key, value)
}
